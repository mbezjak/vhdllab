package hr.fer.zemris.vhdllab.applets.main.components.projectexplorer;

import javax.swing.ImageIcon;

/**
 * enum sadrzi sve ikonice koje koriste cvorovi project explorera
 * 
 * @author Boris Ozegovic
 */
enum Icons {
    VHDL(new ImageIcon("./src/web/onClient/hr/fer/zemris/" +
    		"vhdllab/applets/main/components/projectexplorer/vhdl.png")),
    TB(new ImageIcon("./src/web/onClient/hr/fer/zemris/" +
    		"vhdllab/applets/main/components/projectexplorer/tb.png")), 
    AUTOMAT(new ImageIcon("./src/web/onClient/hr/fer/zemris/" +
    		"vhdllab/applets/main/components/projectexplorer/automat.png")),
    SCHEMA(new ImageIcon("./src/web/onClient/hr/fer/zemris/" +
    		"vhdllab/applets/main/components/projectexplorer/schema.png")), 
    SIMULATION(new ImageIcon("./src/web/onClient/hr/fer/zemris/" +
    		"vhdllab/applets/main/components/projectexplorer/simulation.png"));


    private ImageIcon icon;


    /**
     * Constructor
     */
    Icons(ImageIcon icon) {
        this.icon = icon;
    }


    public ImageIcon getIcon() {
        return icon;
    }
}