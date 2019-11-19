import Word from "../models/Word";
import { Response } from "../models/dto/Response";
import { UpdateWordById } from "../routes/api/words/routes.interface";

// Maybe this should be a Repository or Delegate class, 
//      ... and a Service should refer to this?
export default class WordService {
    async createWord(korean: string, english: string, level: string) {
        try {
            const newWord = await Word.create({
                korean: decodeURIComponent(korean),
                english,
                level: parseInt(level)
            });
            await newWord.save();

            return new Response(newWord.toObject(), "Added new word to database", 200);
        } catch (error) {
            return new Response({ error }, "An unknown error occurred", 500);
        }
    }

    async getWords() {
        try {
            const wordList = await Word.find({}).exec();
            return new Response(wordList, "", 200);
        } catch (error) {
            return new Response({ error }, "An unknown error occurred", 500);
        }
    }

    async getWord(_id: string) {
        try {
            const word = await Word.findOne({ _id }).exec();

            return new Response(word.toObject(), `Word found! id=${_id}`, 200);
        } catch (error) {
            return new Response({ error }, "An unknown error occurred", 500);
        }
    }

    async updateWord(_id: string, updates: UpdateWordById) {
        try {
            // Calculate which things should actually change in the schema
            let changes = Object.keys(updates).reduce((acc, cur) => {
                let keys = [];
                Word.schema.eachPath(path => {
                    keys.push(path);
                });
                if (keys.includes(cur)) {
                    acc[cur] = updates[cur];
                    return acc;
                }
            }, {});

            // Apply changes
            const updatedWord = await Word.findByIdAndUpdate(_id, changes, { runValidators: true, new: true });

            return new Response(updatedWord.toObject(), "Word has been updated", 200);
        } catch (error) {
            return new Response({ error }, "An unknown error occurred", 500);
        }
    }

    async deleteWord(_id: string) {
        try {
            const removedWord = await Word.deleteOne({ _id });
            const { ok, n } = removedWord;

            return new Response({ ok, n }, `${n} records were deleted without error`, 200);
        } catch (error) {
            return new Response({ error }, "An unknown error occurred", 500);
        }
    }
}
