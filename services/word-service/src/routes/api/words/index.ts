import { Router, Request } from "express";
import WordService from "../../../service/word";

import { CreateWord, ReadAllWords, ReadWordById, DeleteById, UpdateWordById } from "./routes.interface";

const WordsRouter = Router();

const wordService = new WordService();

WordsRouter.post("/", async function createNewWord(req: (Request & CreateWord), res) {
    const { korean, english, level } = req.body;
    const result = await wordService.createWord(korean, english, level);
    res.status(result.statusCode).json(result);
});

WordsRouter.get("/", async function getAllWords(req: (Request & ReadAllWords), res) {
    const result = await wordService.getWords();
    res.status(result.statusCode).json(result);
});

WordsRouter.get("/:id", async function getWord(req: (Request & ReadWordById), res) {
    const { id } = req.params;
    const result = await wordService.getWord(id);
    res.status(result.statusCode).json(result);
});

WordsRouter.put("/:id", async function updateWord(req: (Request & UpdateWordById), res) {
    const { id } = req.params;
    const result = await wordService.updateWord(id, req.body);
    res.status(result.statusCode).json(result);
});

WordsRouter.delete("/:id", async function deleteWord(req: (Request & DeleteById), res) {
    const { id } = req.params;
    const result = await wordService.deleteWord(id);
    res.status(result.statusCode).json(result);
});

export default WordsRouter;