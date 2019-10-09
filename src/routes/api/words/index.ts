import { Router, Request } from "express";
import WordService from "../../../service/word";

import { CreateWord, ReadAllWords, ReadWordById, DeleteById, UpdateWordById } from "./routes.interface";

const WordsRouter = Router();

const wordService = new WordService();

WordsRouter.post("/", async (req: (Request & CreateWord), res) => {
    const { korean, english, level } = req.body;
    const result = await wordService.createWord(korean, english, level);
    res.json(result);
});

WordsRouter.get("/", async (req: (Request & ReadAllWords), res) => {
    const result = await wordService.getWords();
    res.json(result);
});

WordsRouter.get("/:id", async (req: (Request & ReadWordById), res) => {
    const { id } = req.params;
    const result = await wordService.getWord(id);
    res.json(result);
});

WordsRouter.put("/:id", async (req: (Request & UpdateWordById), res) => {
    const { id } = req.params;
    const result = await wordService.updateWord(id, req.body);
    res.json(result);
});

WordsRouter.delete("/:id", async (req: (Request & DeleteById), res) => {
    const { id } = req.params;
    const result = await wordService.deleteWord(id);
    res.json(result);
});

export default WordsRouter;