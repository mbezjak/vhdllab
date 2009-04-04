Before adding a new predefined file please read this!

Here is a list of what you need to do in order to add a predefined file:
1) Predefined file must be VHDL source file
    - because of the way MetadataExtractor is implemented for predefined file
      it must only be VHDL source file. This means you can't, for example,
      create a schematic file using VHDLLab and then create a predefined file
      based on created schematic!

2) A predefined file located on a file system must have a name equal to the way
   it is going to be used in VHDLLab! No file system extension can be given to
   such file! Also its name must match entity name used in VHDL entity block of
   describing circuit!
    - for example: a predefined file for AND logic gate is constructed using
      name VL_AND because simply AND is not a correct VHDL entity name. A file
      is constructed whose name is VL_AND (without any .txt or .vhdl
      extension). Note that entity name of described circuit is also VL_AND.

3) Add name of a predefined file to banned list. This means that user will not
   be able to create new file whose name equals your predefined file. Banned
   list is located in a java class under static block, scroll down to block
   commented with "names of predefined files". Class is located in
   vhdllab-common project:
   vhdllab-common/src/main/java/hr/fer/zemris/vhdllab/validation/NameFormatConstraintValidator.java
    - for example: VL_AND predefined file has a line like this: add("VL_AND");
   Note: since this is a source java file you will need to recompile VHDLLab!

4) If you're looking to use predefined file (and most certainly you are) in
   Schematic editor of VHDLLab then you should edit XML file that further
   describes predefined file. For instance, what drawer should be used to draw
   visual representation of predefined file, generic information about a
   circuit, etc. File is located in vhdllab-common project:
   vhdllab-common/src/main/resources/hr/fer/zemris/vhdllab/applets/editor/schema2/predefined/predefined.xml

Note: Any file in this directory (except for this file) is considered
      a predefined file.

Take a look at other predefined files in this directory for any reference on
how to create one.
