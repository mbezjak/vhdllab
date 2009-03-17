package hr.fer.zemris.vhdllab.service.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

public final class Result implements Serializable {

    /*
     * Please note that although this class implements java.io.Serializable and
     * claims to be immutable it does not implement readObject method as
     * specified by Joshua Bloch, "Effective Java: Programming Language Guide",
     * "Item 56: Write readObject methods defensively", page 166.
     * 
     * The reason for this is that this class is used to transfer data from
     * server to client (reverse is not true). So by altering byte stream
     * attacker can only hurt himself!
     */
    private static final long serialVersionUID = 1442733792190834811L;

    private final String data;
    private final List<String> messages;

    @SuppressWarnings("unchecked")
    public Result(String data) {
        this(data, Collections.EMPTY_LIST);
    }

    public Result(List<String> messages) {
        this(null, messages);
    }

    public Result(String data, List<String> messages) {
        Validate.notNull(messages, "Messages can't be null");
        if (messages.isEmpty()) {
            Validate.notNull(data,
                    "Data can't be null if there are no messages");
        }
        this.data = data;
        this.messages = Collections.unmodifiableList(new ArrayList<String>(
                messages));
    }

    public String getData() {
        return data;
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isSuccessful() {
        return messages.isEmpty();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dataLength", data.length())
                .append("messages", messages, false).toString();
    }

}
