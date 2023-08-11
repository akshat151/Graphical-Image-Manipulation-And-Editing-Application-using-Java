Code status:

Symbol Meaning
Y - yes
N - no

Image Editing Application:

Image mosaicking implementation through Command line: Y
Image mosaicking implementation via Script command : Y
Image mosaicing implementation from GUI: Y
Image mosaicing implementation from GUI where the GUI Mosaic option asks for number of seeds to apply to the mosaic implementation from the user: Y

Histogram visualization from GUI: Y

Import/export features:

Load a PPM file implementation: Y
Script command to load a PPM file: Y
Load PPM file from GUI: Y

Load a PNG/JPEG/BMP file implementation: Y
Script command to load a PNG/JPEG/BMP file: Y
Load PNG/JPEG/BMP file from GUI: Y

Save a PPM file implementation: Y
Script command to save a PPM file: Y
Save PPM file from GUI: Y

Save a PNG/JPEG/BMP file implementation: Y
Script command to save a PNG/JPEG/BMP file: Y
Save PNG/JPEG/BMP file from GUI: Y


Project Name: Graphical Image Manipulation And Editing


Contents:

Introduction
Design Changes If Any
Mosaicking Implementation
Citation


Introduction:

This project is a Java Swing-based GUI tool that provides a variety of image processing and editing functionalities via Graphical User Interface and also supports ways to run the application in Script mode or in an interactive text mode using the Jar File.
This tool allows users to execute a range of operations that are specifically tailored to their unique image processing needs. 
The operations can be conveniently performed via GUI or can be performed via the command line or alternatively uploaded through a script file. Currently, 
the tool supports the processing of Conventional image files like (png, bmp, jpg) and ascii ppm image files. However, the project is designed with extensibility in mind, 
with the capacity to support other image file types in the future by simply extending the existing MVC design.

Design Changes If Any:

We added mosaic(int numOfSeeds) method in the existing ImageInterface and added the mosaic(int numOfSeeds) method implimentation in the existing ImageImpl class which implements the ImageInterface.
We had to make changes in the existing interface of ImageInterface because ImageInterface was tightly coupled with the rest of the design and any attempts in extending this existing ImageInterface with new ImageInterfaceExtention interface would have led to changes in approximately more than 200 exisitng implementation and test methods which would have vialoted the Open-Close principle and could have lead to some spill over effects in the existing implementation, so it was better to just add the new mosaic(int numOfSeeds) method in the existing ImageInterface only and provide its implementation in the exisitng ImageImpl class which implements the ImageInterface.

This would reduce the number of changes required to support the image mosaicking method in the exisiting codebase.


Mosaicking Implementation :

Steps to implement Image Mosaicking:

In Model Section :-

1. Added the mosaic(int numOfSeeds) method in the existing ImageInterface;
2. Added the mosaic(int numOfSeeds) method implimentation in the existing ImageImpl class which implements the ImageInterface.


In Controller Section :-

1. Added the mosaic command class which implements the existing ImageCommandInterface;
2. Added the mosaic command class constructor in the AbstractController command map.

In View Section :-

1. Added the mosaic feature in the dropdown control of the existing JComboBox operationDrop of GraphicalView Class.
2. Added the mosaic action listeners in the existing manageListeners method of GraphicalView Class.
3. Added the mosaic command set in the existing setup(Set<String> commandSet) method of GraphicalView Class.
4. Added showInputDialog() method in the GraphicalView Class to get the number of seeds to apply to the image for mosaicking from the user.
