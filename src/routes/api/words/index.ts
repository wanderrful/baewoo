import { Router } from "express";
import { getWordModel } from "../../../models/Word";
import { BaewooResponse } from "../../../models/dto/Response";
import { CreateWord, ReadAllWords, ReadWordById, DeleteById, UpdateWordById } from "./routes.interface";
import { handleError } from "../../shared";

const getWordsRouter = async () => {
    const WordsRouter = Router();

    WordsRouter.post("/", async (req: CreateWord, res) => {
        const { korean, english, level } = req.body;
        try {
            const word = await getWordModel();
            const newWord = await word.create({
                korean: decodeURIComponent(korean),
                english,
                level: parseInt(level)
            });
            await newWord.save();
            word.db.close();
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
        const word = await getWordModel();
        const result = await word.find({}).exec();
        res.send(result);
        res.end();
        word.db.close();
    });

    WordsRouter.get("/:id", async (req: ReadWordById, res) => {
        const { id } = req.params;
        const word = await getWordModel();
        const result = await word.findOne({ _id: id }).exec();
        res.send(result);
        res.end();
        word.db.close();
    });

    WordsRouter.put("/:id", async (req: UpdateWordById, res) => {
        const { id } = req.params;
        try {
            const word = await getWordModel();
            if (!Object.keys(req.body).length) throw new Error("No arguments provided");
            let changes = Object.keys(req.body).reduce((acc, cur) => {
                let keys = [];
                word.schema.eachPath(path => {
                    keys.push(path);
                });
                if (keys.includes(cur)) {
                    acc[cur] = req.body[cur];
                    return acc;
                }
            }, {});
            await word.findByIdAndUpdate(id, changes, { runValidators: true });
            word.db.close();
            res.end();
        } catch (e) {
            handleError(res, e);
        }
    });

    WordsRouter.delete("/:id", async (req: DeleteById, res) => {
        const { id } = req.params;
        try {
            const word = await getWordModel();
            const removedWord = await word.deleteOne({
                _id: id
            });
            const { ok, n } = removedWord;
            if (ok !== 1 || n === 0) {
                throw new Error(`Failed to delete (${n} items found)`);
            }
            word.db.close();
            res.send(new BaewooResponse({
                data: { ok },
                message: `Removed ${n} word(s) from database`
            }).render());
            res.end();
        } catch (e) {
            handleError(res, e);
        }
    });

    return WordsRouter;
};

export { getWordsRouter };