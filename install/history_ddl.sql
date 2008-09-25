CREATE OR REPLACE VIEW project_history_ordered AS
    SELECT user_id, name, id AS project_id, created_on, deleted_on, insert_version
        FROM project_history
        ORDER BY user_id, name;

CREATE OR REPLACE VIEW project_history_count AS
    SELECT user_id, COUNT(*) AS project_count
        FROM project_history_ordered
        GROUP BY 1;

CREATE OR REPLACE VIEW file_history_ordered AS
    SELECT project_id, type, name, insert_version, update_version, created_on, deleted_on
        FROM file_history
        WHERE project_id IN (SELECT DISTINCT p.project_id FROM project_history_ordered AS p)
        ORDER BY project_id, name, insert_version, update_version;

CREATE OR REPLACE VIEW file_history_update_version_count AS
    SELECT project_id, name, insert_version, COUNT(*) AS update_version_count
        FROM file_history_ordered
        GROUP BY 1, 2, 3;

CREATE OR REPLACE VIEW file_history_insert_version_count AS
    SELECT project_id, name, COUNT(*) AS insert_version_count
        FROM file_history_ordered
        WHERE update_version = 0
        GROUP BY 1, 2;

CREATE OR REPLACE VIEW file_history_total_file_count AS
    SELECT project_id, COUNT(*) AS total_file_count
        FROM file_history_ordered
        WHERE update_version = 0
        GROUP BY 1;

CREATE OR REPLACE VIEW file_history_count AS
    SELECT project_id, COUNT(*) AS file_history_count
        FROM file_history_ordered
        GROUP BY project_id;

CREATE OR REPLACE VIEW file_count_current AS
    SELECT project_id, COUNT(*) AS file_count
        FROM file_history_ordered AS fo
        WHERE deleted_on IS NULL
            AND insert_version = (SELECT max(insert_version)
                                    FROM file_history AS fh
                                    WHERE fh.project_id = fo.project_id
                                        AND fh.name = fo.name)
            AND update_version = (SELECT max(update_version)
                                    FROM file_history AS fh
                                    WHERE fh.project_id = fo.project_id
                                        AND fh.name = fo.name
                                        AND fh.insert_version = fo.insert_version)
        GROUP BY 1;


CREATE OR REPLACE VIEW file_changes_current AS
    SELECT project_id, COUNT(*) AS file_count
        FROM file_history_ordered AS fo
        WHERE deleted_on IS NULL
            AND insert_version = (SELECT max(insert_version)
                                    FROM file_history AS fh
                                    WHERE fh.project_id = fo.project_id
                                        AND fh.name = fo.name)
        GROUP BY 1;

CREATE OR REPLACE VIEW history_statistics AS
    SELECT  po.user_id,
            pc.project_count,
            po.name AS project_name,
            po.project_id,
            po.created_on AS project_created_on,
            po.deleted_on AS project_deleted_on,
            IFNULL(fc.file_history_count, 0) AS total_project_changes,
            IFNULL(ftc.total_file_count, 0) AS total_file_count,
            IFNULL(fc.file_history_count / ftc.total_file_count, 0) AS total_avg_change,
            IFNULL(fhc.file_count, 0) AS currect_project_changes,
            IFNULL(fcc.file_count, 0) AS current_file_count,
            IFNULL(fhc.file_count / fcc.file_count, 0) AS current_avg_change,
            IFNULL(fc.file_history_count / fhc.file_count, 1) - 1 AS deletion_impact
        FROM project_history_ordered AS po
            LEFT OUTER JOIN project_history_count pc ON pc.user_id = po.user_id
            LEFT OUTER JOIN file_history_count fc    ON fc.project_id = po.project_id
            LEFT OUTER JOIN file_changes_current fhc ON fhc.project_id = po.project_id
            LEFT OUTER JOIN file_count_current fcc   ON fcc.project_id = po.project_id
            LEFT OUTER JOIN file_history_total_file_count ftc ON ftc.project_id = po.project_id


