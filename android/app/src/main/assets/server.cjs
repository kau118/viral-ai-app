var __create = Object.create;
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __getProtoOf = Object.getPrototypeOf;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
  // If the importer is in node compatibility mode or this is not an ESM
  // file that has been converted to a CommonJS file using a Babel-
  // compatible transform (i.e. "__esModule" has not been set), then set
  // "default" to the CommonJS "module.exports" for node compatibility.
  isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
  mod
));

// server.ts
var import_express = __toESM(require("express"), 1);
var import_path = __toESM(require("path"), 1);
var import_dotenv = __toESM(require("dotenv"), 1);
var import_cors = __toESM(require("cors"), 1);
var import_vite = require("vite");
var import_generative_ai = require("@google/generative-ai");
import_dotenv.default.config();
var app = (0, import_express.default)();
app.use((0, import_cors.default)({
  origin: "*",
  methods: ["GET", "POST", "OPTIONS"],
  allowedHeaders: ["Content-Type", "Authorization", "Accept"],
  credentials: true
}));
app.use(import_express.default.json());
var PORT = process.env.PORT || 3e3;
var genAI = null;
function getGenAI() {
  if (!genAI) {
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      throw new Error("GEMINI_API_KEY is not configured.");
    }
    genAI = new import_generative_ai.GoogleGenerativeAI(apiKey);
  }
  return genAI;
}
app.get("/api/health", (req, res) => {
  const hasKey = !!process.env.GEMINI_API_KEY;
  res.json({ status: "ok", geminiKeyAvailable: hasKey });
});
app.post("/api/login", (req, res) => {
  const { email, password } = req.body;
  if (email && password) {
    res.json({ status: "success", user: { email, id: "vai_user_1" } });
  } else {
    res.status(400).json({ error: "Email and password required" });
  }
});
app.post("/api/generate-all", async (req, res) => {
  try {
    const { topic, vibe } = req.body;
    if (!topic) {
      return res.status(400).json({ error: "Topic is required." });
    }
    const ai = getGenAI();
    const model = ai.getGenerativeModel({ model: "gemini-1.5-flash" });
    const prompt = `Generate a complete viral content package for: "${topic}" (Vibe: ${vibe || "Professional"}).
    Format response as JSON:
    {
      "hook": "Killer opening line",
      "caption": "Engaging caption with emojis",
      "script": "30s storyboard script",
      "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"]
    }`;
    const result = await model.generateContent(prompt);
    const response = await result.response;
    let text = response.text();
    text = text.replace(/```json/g, "").replace(/```/g, "").trim();
    const data = JSON.parse(text);
    res.json(data);
  } catch (error) {
    console.error("AI Error:", error);
    res.status(500).json({
      error: "AI Generation Failed",
      details: error.message
    });
  }
});
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    const vite = await (0, import_vite.createServer)({
      server: { middlewareMode: true },
      appType: "spa"
    });
    app.use(vite.middlewares);
  } else {
    const distPath = import_path.default.join(process.cwd(), "dist");
    app.use(import_express.default.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(import_path.default.join(distPath, "index.html"));
    });
  }
  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on port ${PORT}`);
  });
}
startServer();
//# sourceMappingURL=server.cjs.map
