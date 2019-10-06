import { Router } from "express";
import Word from "../../../models/Word";
import { BaewooResponse } from "../../../models/dto/Response";
import { CreateWord, ReadAllWords, ReadWordById, DeleteById, UpdateWordById } from "./routes.interface";
import { handleError } from "../../shared";

const WordsRouter = Router();

WordsRouter.post("/", async (req: CreateWord, res) => {
    const { korean, english, level } = req.body;
    try {
        const newWord = await Word.create({
            korean: decodeURIComponent(korean),
            english,
            level: parseInt(level)
        });
        await newWord.save();

        res.send(new BaewooResponse({
            data: newWord.toJSON(),
            message: "Added new word to database"
        }).render());
        res.end();
    } catch (e) {
        handleError(res, e);
    }
});

WordsRouter.get("/", async (req: ReadAllWords, res) => {
    const result = await Word.find({}).exec();
    res.send(result);
    res.end();
});

WordsRouter.get("/:id", async (req: ReadWordById, res) => {
    const { id } = req.params;
    const result = await Word.findOne({ _id: id }).exec();
    res.send(result);
    res.end();
});

WordsRouter.put("/:id", async (req: UpdateWordById, res) => {
    const { id } = req.params;
    try {
        if (!Object.keys(req.body).length) throw new Error("No arguments provided");
        let changes = Object.keys(req.body).reduce((acc, cur) => {
            let keys = [];
            Word.schema.eachPath(path => {
                keys.push(path);
            });
            if (keys.includes(cur)) {
                acc[cur] = req.body[cur];
                return acc;
            }
        }, {});
        await Word.findByIdAndUpdate(id, changes, { runValidators: true });
        res.end();
    } catch (e) {
        handleError(res, e);
    }
});

WordsRouter.delete("/:id", async (req: DeleteById, res) => {
    const { id } = req.params;
    try {
        const removedWord = await Word.deleteOne({
            _id: id
        });
        const { ok, n } = removedWord;
        if (ok !== 1 || n === 0) {
            throw new Error(`Failed to delete (${n} items found)`);
        }

        res.send(new BaewooResponse({
            data: { ok },
            message: `Removed ${n} word(s) from database`
        }).render());
        res.end();
    } catch (e) {
        handleError(res, e);
    }
});

export default WordsRouter;