# Lipid Calculator
The application includes a lipid calculator and an adduct transformer, both of which enhance the analytical study of metabolite mass spectrometry research. The lipid calculator is designed as an advanced search tool, equipped with an intuitive graphical interface that significantly improves user experience. The adduct transformer, located on a separate interface within the application, facilitates several critical functions such as finding M from m/z values and vice versa and the creation of unique adducts for personal use by the user, adhering to the recognized regular expression format of an adduct defined as [M+X]Q+/-.

## Table of Contents
- [Usage](#usage)
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Contact](#contact)

## Usage
### Lipid Calculator
The lipid calculator interface enables users to input single data points or conduct batch processing, streamlining the analysis of extensive datasets and their resultant outputs. The primary output is a detailed table that showcases the lipids associated with the given data inputs. This dataset includes various lipid groups that can be derived from the previously introduced values based on the selected adducts.
### Adduct Transformer
The adduct transformer interface enables users to:
  1.	Determine M given the m/z values.
  2.	Calculate the m/z values given M.
  3.	Create personalized adducts for use within the system.

## Installation
### Requirements
- JDK 23

### Steps
1. **Clone the repository**:
   ```bash
   git clone https://github.com/ceu-biolab/compounds-calculator.git
   cd compounds-calculator
2. **Build the project with Maven**: Maven will automatically download all dependencies specified in `pom.xml`.
    ```bash
    mvn clean install
    ```
3. **Run the application**:
   Run the compiled JAR directly:
     ```bash
     java -jar target/compounds-calculator.jar
     ```

### Database Setup
The application will automatically create an SQLite database file (`compounds.db`) in the root directory upon the first run. Ensure the application has write permissions to create and manage this file.

## Contact
Pilar Bourg - pilar.bourg@usp.ceu.es
Project link: https://github.com/ceu-biolab/compounds-calculator.git
