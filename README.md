# AI Insight Vault

AI Insight Vault is a cross-platform application built with **Compose Multiplatform** designed to capture, organize, and refine AI-generated insights. The project focuses on a provider-agnostic architecture, allowing users to scrape and preserve intellectual threads from any web-based AI interface while maintaining meaningful human control over the data.

## 🚀 Project Vision
As AI interaction becomes a primary mode of research and development, capturing the "insight" behind the prompt is critical. This application acts as a secure, local repository (Vault) that transforms ephemeral web conversations into structured, searchable data.

## ✨ Key Features (Sprint 4 Update)
* **Agnostic Web Capture:** Utilizes a JavaScript-injected bridge to extract content from any text-based web interface (Gemini, ChatGPT, Claude, etc.) without relying on brittle, provider-specific CSS selectors.
* **Intelligent Auto-Titling:** Automatically generates unique identifiers based on capture timestamps, ensuring immediate data integrity.
* **Human-in-the-Loop Refinement:**
    * **Editable Titles:** Users can rename insights during the capture phase for better organization.
    * **Interactive Metadata:** Suggested tags are presented as interactive chips; users can prune AI-generated tags or append custom metadata on the fly.
* **Decoupled Summarization Pipeline:** Implements an interface-driven service layer. The current build utilizes a `MockSummarizationService` to validate asynchronous UI flows and loading states, ensuring the app is ready for local LLM integration.
* **Robust Persistence:** Powered by a Room database with a flat, agnostic schema and a custom SQLite driver optimized for multiplatform performance.

## 🏗️ Architecture & Pipeline
The application follows a **"Data Funnel"** architecture to ensure data quality:
1.  **Capture Stage:** Raw text extraction via the WebView bridge.
2.  **Processing Stage:** Asynchronous summarization and keyword extraction through a decoupled service layer.
3.  **Refinement Stage:** UI-driven human intervention for title and tag verification.
4.  **Persistence Stage:** Final commit to the local SQLite vault using the Room DAO pattern.

## 🛠️ Tech Stack
* **Language:** [Kotlin](https://kotlinlang.org/) (100%)
* **Framework:** [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
* **UI/UX:** Jetpack Compose, Material 3
* **Database:** [Room](https://developer.android.com/training/data-storage/room) (with Bundled SQLite Driver for KMP)
* **Concurrency:** Kotlin Coroutines
* **Time:** kotlinx-datetime

## 📂 Project Structure
* `commonMain`: Core UI logic, the `SummarizationService` interface, and the `InsightEntity` definitions.
* `androidMain`: Android-specific entry point and Room database initialization.
* `database`: DAO (Data Access Object) and Database configuration implementing `OnConflictStrategy.REPLACE`.
* `service`: Service implementations including the `MockSummarizationService`.

## ⚙️ Setup & Installation
1.  Clone the repository.
2.  Open in **Android Studio (Panda 4 or newer)**.
3.  Ensure the **Kotlin Multiplatform** plugin is installed and updated.
4.  Sync Gradle and run the `:composeApp` module on an Android Emulator (API 31+ recommended).

## 🔮 Future Roadmap
- [ ] **Phase 4:** Integration of **LiteRT-LM** (Gemma 2b) for local, on-device summarization.
- [ ] **Phase 5:** Implementation of a side panel for data retrieval and filtering, secure login/security integration, and final UI/UX refinement.

---
*Developed as part of my Software Development and Integration, and Applied Artificial Intelligence research tracks at the OU Polytechnic Institute.*