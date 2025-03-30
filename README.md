# 📸 GRAPHICAL IMAGE MANIPULATION AND EDITING

A powerful Java-based image editing application that supports a wide range of features for image manipulation through a **Graphical User Interface (GUI)**, **Command-Line Interface (CLI)**, and **Script-based automation**. Designed with extensibility in mind, the project follows a robust **Model-View-Controller (MVC)** architecture, making it adaptable for future enhancements.

---

## ✅ FEATURE OVERVIEW

### Supported Functionalities

| Feature                             | Command Line | Script | GUI |
|-------------------------------------|--------------|--------|-----|
| Image Mosaicking                    | ✅           | ✅     | ✅  |
| Mosaic with Seed Input via GUI      | ❌           | ❌     | ✅  |
| Histogram Visualization             | ❌           | ❌     | ✅  |

### 📤 Import / Export Support

| File Format       | Load (Script) | Load (GUI) | Save (Script) | Save (GUI) |
|-------------------|---------------|------------|----------------|------------|
| PPM               | ✅            | ✅         | ✅             | ✅         |
| PNG / JPEG / BMP  | ✅            | ✅         | ✅             | ✅         |

---

## 🚀 PROJECT HIGHLIGHTS

- ✅ **Mosaicking Functionality:**
  - Fully integrated image mosaicking feature
  - Supports seed-based mosaic customization via GUI input
  - Available across all execution modes: GUI, CLI, and script

- ✅ **Cross-Format Support:**
  - Seamless loading and saving of `.ppm`, `.png`, `.jpg`, and `.bmp` formats
  - Unified experience across all interaction modes

- ✅ **Script & CLI Automation:**
  - Efficient batch processing via scripts or command line
  - Ideal for repetitive or headless processing tasks

- ✅ **Extensible Design:**
  - Easily extendable to support additional image formats or features
  - Clean separation of logic and UI via MVC architecture

---

## 🛠️ DESIGN DECISIONS & CHANGES

### Mosaic Method Integration

To implement the `mosaic(int numOfSeeds)` functionality:

- **Model:**
  - Added `mosaic(int numOfSeeds)` to `ImageInterface`
  - Implemented it in the `ImageImpl` class

> 💡 **Why not use an extension interface?**  
> Creating a new interface (e.g., `ImageInterfaceExtension`) would have affected over 200 files and violated the **Open-Closed Principle**. Direct integration minimized changes and risk to the existing codebase.

---

## 🧩 MOSAICKING IMPLEMENTATION DETAILS

### Model
- ✅ `ImageInterface`: added `mosaic(int numOfSeeds)`
- ✅ `ImageImpl`: implemented the method

### Controller
- ✅ Created `MosaicCommand` class (`ImageCommandInterface`)
- ✅ Registered the new command in `AbstractController`

### View
- ✅ Added "Mosaic" to the `operationDrop` dropdown in `GraphicalView`
- ✅ Implemented listeners in `manageListeners()`
- ✅ Updated `setup(Set<String>)` to include the new command
- ✅ Prompted user for seed input using `JOptionPane.showInputDialog`

---

## 📐 CLASS DIAGRAM

📌 Add your class diagram below:

![Class Diagram](screenshots/class-diagram.png)

---

## 🖼️ SCREENSHOTS

### GUI Main Window  
![Main Window](screenshots/main-window.png)

### Mosaic Feature in Action  
![Mosaic Feature](screenshots/mosaic-preview.png)

### Histogram Visualization  
![Histogram](screenshots/histogram-visualization.png)

---

## 📚 CITATION

If you use this tool or build upon it, please cite this project appropriately.
