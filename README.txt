📸 Graphical Image Manipulation and Editing

A powerful Java-based image editing application that supports a wide range of features for image manipulation through a Graphical User Interface (GUI), Command-Line Interface (CLI), and Script-based automation. Designed with extensibility in mind, the project follows a robust Model-View-Controller (MVC) architecture, making it adaptable for future enhancements.

⸻

✅ Feature Overview

Feature	Command Line	Script	GUI
Image Mosaicking	✔️	✔️	✔️
Mosaic with Seed Input via GUI	✖️	✖️	✔️
Histogram Visualization	✖️	✖️	✔️

📤 Import / Export Support

File Format	Load (Script)	Load (GUI)	Save (Script)	Save (GUI)
PPM	✔️	✔️	✔️	✔️
PNG / JPEG / BMP	✔️	✔️	✔️	✔️



⸻

🚀 Project Highlights
	•	Mosaicking Functionality:
	•	Fully integrated image mosaicking feature.
	•	Supports seed-based mosaic customization via GUI input dialog.
	•	Available across all execution modes: GUI, CLI, and script.
	•	Cross-Format Support:
	•	Seamlessly load and save in .ppm, .png, .jpg, and .bmp formats.
	•	Uniform interface across all modes of interaction.
	•	Script and CLI Automation:
	•	Efficient batch processing via custom scripts or CLI.
	•	Ideal for repetitive or headless tasks.
	•	Extensible Design:
	•	Easily extendable to support new formats or features via MVC.
	•	Clean separation of logic, control, and UI for future scalability.

⸻

🛠️ Design Decisions & Changes

Mosaic Method Integration

To integrate the mosaic(int numOfSeeds) functionality efficiently:
	•	Model:
	•	mosaic(int numOfSeeds) was directly added to the existing ImageInterface.
	•	Implemented in ImageImpl, ensuring backward compatibility and minimal disruption.

💡 Rationale:
Adding a new interface (e.g., ImageInterfaceExtension) would have required modifications across 200+ classes, breaking the Open/Closed Principle and introducing potential regressions. Direct integration into the base interface was the most pragmatic and stable solution.

⸻

🧩 Mosaicking Implementation Details

Model
	•	ImageInterface updated with mosaic(int numOfSeeds)
	•	ImageImpl class implements this method

Controller
	•	New MosaicCommand class created, implementing ImageCommandInterface
	•	Registered MosaicCommand in AbstractController’s command map

View
	•	Added Mosaic to operationDrop (JComboBox) in GraphicalView
	•	Implemented action listener via manageListeners() in GraphicalView
	•	Added setup() support for mosaic command in view
	•	Created a user prompt (JOptionPane.showInputDialog) for seed input

⸻

🖼️ Class Diagram

📌 [Insert class diagram here]
Create the class diagram using draw.io, Lucidchart, or PlantUML and add the exported image here:

![Class Diagram](screenshots/class-diagram.png)


⸻

📚 Citation

If you use this tool or build upon it, please cite this project appropriately.

⸻
