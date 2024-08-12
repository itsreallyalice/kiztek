# KiZTeK

**KiZTeK: An Easy-to-Use LaTeX Editor and Compiler**

## Overview

KiZTeK is a user-friendly, offline LaTeX editor designed to simplify the process of creating high-quality LaTeX documents without requiring users to have extensive programming knowledge. The project was developed to address the challenges faced by beginners in using traditional LaTeX editors and to offer a more accessible tool for both novice and experienced users.

## Features

- **Integrated Text Editor**: A simple and intuitive text editor with LaTeX syntax highlighting.
- **PDF Viewer**: Real-time preview of the compiled LaTeX document within the editor.
- **File Manager**: Easy management of LaTeX project files directly from the sidebar.
- **Toolbar & Menu Bar**: Quick access to commonly used LaTeX commands and editor functions.
- **Offline Functionality**: Full functionality without the need for an internet connection.
- **Customizable Settings**: Tailor the editor to your preferences, including setting the main TeX file and compiler options.

## Installation

To set up KiZTeK locally, please ensure you have a LaTeX installation (Tex Live reccomended) and follow these steps:

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/kiztek.git
    cd kiztek
    ```

2. **Build the Project:**
    Ensure you have Maven installed. Then, run:
    ```bash
    mvn clean install
    ```

3. **Run the Application:**
    Start the editor by executing:
    ```bash
    java -jar target/kiztek.jar
    ```

## Usage

- **Creating a New Project**: Use the "New" menu item in the "File" menu bar to start a new project.
- **Opening a Project**: Use the "Open" menu item in the "File" menu bar to start a new project
- **Compiling LaTeX**: Click the "Recompile" button in the toolbar to generate the PDF output.
- **Saving Your Work**: Use the "Save" button or press `Ctrl+S` to save changes to your `.tex` files.
- **Viewing PDFs**: The integrated PDF viewer updates in real-time as you edit and compile your LaTeX files.

## Future Work

Potential enhancements include:

- **Cloud Sync**: Integration with cloud services for collaborative editing.
- **Advanced LaTeX Templates**: Pre-designed templates for common document types.
- **Cross-Platform Support**: Expanding compatibility beyond the current platform.

## Contributing

Contributions are welcome! Please fork the repository, create a new branch, and submit a pull request with your changes. Make sure to follow the project's code of conduct.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

Special thanks to the University of Sussex and all contributors for their support in developing KiZTeK.

