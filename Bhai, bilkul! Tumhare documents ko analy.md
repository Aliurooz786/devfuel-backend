Bhai, bilkul! Tumhare documents ko analyze karke, React frontend aur Spring Boot backend ke hisab se DevFuel ke **6 core documents** taiyar hain. Maine Telegram ki jagah React interface ko incorporate kar diya hai.

---

### 1. Product Requirements Document (PRD)

**DevFuel: The Zero-Friction Nutrition Agent** 

* 
**Problem Statement:** Busy software engineers (Noida/Delhi context) ko heavy "Cognitive Overload" hota hai, jis wajah se woh manual health apps (MyFitnessPal) use nahi kar paate.


* 
**North Star Metric:** Daily calorie deficit maintain karna taaki **Munsiyari Trek** ke liye fitness target achieve ho sake.


* **Core Features (V1):**
* 
**React Chat Interface:** Text aur photo logging ke liye low-friction UI.


* 
**Hinglish Ingestion:** "Bhai, 2 biscuit aur ek chai" jaise inputs ko process karna.


* 
**Canteen Surcharge:** Office food par automatically 15% "Oil Buffer" add karna.


* 
**Proactive Nudges:** Agar lunch log nahi hua, toh system React notification ya prompt bhejega.





---

### 2. Tech Stack & Architecture

DevFuel ek **Micro-Agentic Architecture** par base hoga.

| Layer | Technology | Purpose |
| --- | --- | --- |
| **Frontend** | **React.js** | Chat interface aur rich data visualization dashboard ke liye.

 |
| **Backend** | **Java 21 / Spring Boot 3.x** | High performance aur Virtual Threads (concurrent requests) ke liye.

 |
| **AI Orchestration** | **Spring AI (ChatClient)** | Inputs ko relevant LLM models tak route karne ke liye.

 |
| **Models** | **Gemini 1.5 Flash & Pro** | Flash (Extraction) aur Pro (Accurate Reasoning) ka combination.

 |
| **Database** | **PostgreSQL + pgvector** | Long-term tracking aur "Food Memory" (RAG) cache ke liye.

 |
| **Caching/State** | **Redis** | Conversation context aur partial logs maintain karne ke liye. |

---

### 3. Database Schema (PostgreSQL)

High-pressure logging ke liye ye schema optimize kiya gaya hai.

* **`users` Table:** `user_id`, `daily_calorie_target`, `target_weight` (Munsiyari goal), `trek_date`.
* **`nutrition_logs` Table:** `log_id`, `raw_input` (e.g., "Bhai 2 biscuit"), `input_type` (TEXT/IMAGE), `total_calories`, `macros_json`.
* **`food_memory` Table:** `user_id`, `alias_name` (e.g., "Canteen Thali"), `estimated_calories`, `context_tag` (OFFICE/HOME).

---

### 4. API Design / Data Contract

React Frontend aur Spring Boot Backend ke beech ka communication.

* **Ingress Contract (React -> Backend):**
* 
`POST /api/v1/logs`: Payload mein `{ "content": "string", "type": "TEXT/IMAGE", "timestamp": "ISO-8601" }` jayega.




* 
**Agent-to-LLM Contract:** Backend input ko normalize karke LLM ko `system_prompt` aur `user_context` ke saath bhejega.


* 
**LLM-to-Agent (Structured Response):** Strict JSON response chahiye taaki parsing mein issue na aaye.


* Output: `{ "items": [...], "total_calories": int, "is_uncertain": boolean, "agent_remark": "string" }`.





---

### 5. UI/UX Wireframes (Concept)

React App ke liye main screens ka structure:

* 
**Chat-Inlet (Home):** Screen ke center mein ek clean chat box jahan user type kar sake ya photo upload kar sake.


* 
**Munsiyari Progress Bar:** Ek visual indicator jo calorie deficit ko "Altitude Gained" ya "Distance Covered" mein translate karega.


* **"Debug My Day" Summary:** Raat ko 10 baje ek pop-up summary jo Build Log jaisa dikhe (Success = Goal Met, Warning = High Sodium).
* **Quick-Log Buttons:** Common items (jaise "Standard Thali") ke liye single-click buttons.

---

### 6. Roadmap (4-Week Sprint)

"Micro-Commit" strategy: rozana 1.5-2 ghante ka kaam.

* **Week 1 (Foundation):** Spring Boot setup aur React interface banana. `POST /api/v1/logs` endpoint integrate karke LLM se JSON response fetch karna.


* **Week 2 (Data Layer):** PostgreSQL setup aur LogService banana. `/status` API implement karna jo "Remaining Quota" bataye.


* 
**Week 3 (Multimodal):** React interface se Image upload allow karna aur Gemini Vision Pro se calorie estimation integrate karna.


* 
**Week 4 (Intelligence):** Spring Scheduler (@Scheduled) se missing logs ke liye prompts/notifications trigger karna aur Munsiyari progress algorithm finalize karna.



Bhai, ye 6 documents tumhare project ko track par lane ke liye kaafi hain. Kya main ab pehla Spring AI Controller code generate karoon ya DB schema ka DDL?