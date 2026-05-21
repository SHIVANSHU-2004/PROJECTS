# AI Medical Symptoms Checker

A Java desktop application that lets a user enter symptoms, review possible health conditions, get safe medicine/self-care guidance, and save a basic health report.

> Important: This project is for educational support only. It is not a medical diagnosis, prescription, or replacement for a licensed doctor. For chest pain, breathing difficulty, fainting, stroke symptoms, severe allergic reaction, uncontrolled bleeding, or any emergency, seek urgent medical care immediately.

## User Interface

![AI Medical Symptoms Checker UI](screenshots/user-interface.png)

## Features

- Enter patient name, age, and symptoms.
- Analyze symptoms using an AI API when an API key is configured.
- Uses a local rule-based fallback when no API key is available.
- Suggests possible conditions to discuss with a doctor.
- Gives safe over-the-counter medicine category and self-care guidance.
- Shows urgent care warning signs.
- Saves the generated health report as a text file.
- Includes a console demo for quick output testing.

## Technology Used

- Java
- Java Swing for the user interface
- Java HTTP Client for AI API calls
- Optional OpenAI-compatible chat completions API

## Project Structure

```text
AI Medical Symptoms Checker/
├── README.md
├── run.bat
├── run.ps1
├── screenshots/
│   └── user-interface.png
└── src/
    └── main/
        └── java/
            └── com/
                └── symptomchecker/
                    ├── AiMedicalClient.java
                    ├── DemoOutput.java
                    ├── HealthReport.java
                    ├── Main.java
                    ├── RuleBasedMedicalAdvisor.java
                    ├── ScreenshotGenerator.java
                    └── SymptomCheckerFrame.java
```

## How To Run The User Interface

Open PowerShell inside this folder and run:

```powershell
.\run.ps1
```

If PowerShell blocks scripts, run:

```powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1
```

You can also run it from Command Prompt:

```bat
run.bat
```

## How To Run Console Output

Compile the project:

```powershell
javac -d out src\main\java\com\symptomchecker\*.java
```

Run the sample output:

```powershell
java -cp out com.symptomchecker.DemoOutput
```

Run with your own symptoms:

```powershell
java -cp out com.symptomchecker.DemoOutput "fever cough headache sore throat"
```

## AI API Setup

The app works without an API key using the built-in local guidance engine.

To enable AI API mode, set an environment variable before running:

```powershell
$env:AI_API_KEY="your_api_key_here"
.\run.ps1
```

Optional settings:

```powershell
$env:AI_API_URL="https://api.openai.com/v1/chat/completions"
$env:AI_MODEL="gpt-4o-mini"
```

Do not write private API keys directly in the source code.

## Example Output

```text
Sample symptoms: fever, cough, headache, sore throat

Possible conditions to discuss with a doctor:
- Viral infection, flu, COVID-like illness, malaria/dengue risk depending on location and exposure
- Common cold, throat infection, allergies, bronchitis, or flu-like illness
- Tension headache, migraine, dehydration, sinus infection, eye strain, or high blood pressure

Medicine / care suggestions:
- Rest, drink fluids, monitor temperature, and consider paracetamol/acetaminophen as per the medicine label if safe for you
- Warm fluids, steam inhalation, saline gargle/spray, honey for cough if age is above 1 year
- Hydration, rest in a quiet room, reduce screen brightness, and use common pain relief only if safe for you

When to seek urgent care:
- Fever above 103 F / 39.4 C, fever lasting more than 3 days, stiff neck, confusion, rash, or severe weakness
- Shortness of breath, blue lips, chest pain, blood in cough, or oxygen problems
- Sudden worst headache, head injury, weakness on one side, vision loss, fainting, or high fever with neck stiffness
```

## Main Classes

- `Main.java`: Starts the Swing user interface.
- `SymptomCheckerFrame.java`: Builds the desktop UI.
- `AiMedicalClient.java`: Calls the AI API or falls back to local mode.
- `RuleBasedMedicalAdvisor.java`: Provides local medical guidance when no API key is set.
- `HealthReport.java`: Formats saved reports.
- `DemoOutput.java`: Prints sample console output.
- `ScreenshotGenerator.java`: Generates the UI image used in this README.

## Disclaimer

This software is an educational project. It should not be used to make medical decisions without consulting a qualified healthcare professional.
