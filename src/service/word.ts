import Word from "../models/Word";
import { Response } from "../models/dto/Response";
import { UpdateWordById } from "../routes/api/words/routes.interface";

// Maybe this should be a Repository or Delegate class, 
//      ... and a Service to refer to this?
export default class WordService {
    async createWord(korean: string, english: string, level: string) {
        let response = new Response();

        const newWord = await Word.create({
            korean: decodeURIComponent(korean),
            english,
            level: parseInt(level)
        });
        await newWord.save();

        response.data = newWord.toObject();
        response.message = "Added new word to database";

        return response;
    }

    async getWords() {
        const wordList = await Word.find({}).exec();
        return wordList;
    }

    async getWord(_id: string) {
        let response = new Response();

        if (!_id) {
            response.message = "No ID provided!";
            return response;
        }

        const word = await Word.findOne({ _id }).exec();
        if (!word) {
            response.message = `Word not found! id=${_id}`;
            return response;
        }

        response.data = word.toObject();
        response.message = `Word found! id=${_id}`;

        return response;
    }

    async updateWord(_id: string, updates: UpdateWordById) {
        let response = new Response({}, "");

        if (!Object.keys(updates).length) {
            response.message = "No arguments were provided!";
            return response;
        }

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
        const updatedWord = await Word.findByIdAndUpdate(_id, changes, { runValidators: true });

        response.data = updatedWord.toObject();
        response.message = "Word has been updated";

        return response;
    }

    async deleteWord(_id: string) {
        let response = new Response();

        if (!_id) {
            response.message = "No ID provided!";
            return response;
        }

        const removedWord = await Word.deleteOne({ _id });

        const { ok, n } = removedWord;
        response.data = { ok };
        if (ok !== 1 || n === 0) {
            response.message = `Failed to delete (${n} items found)`;
            return response;
        }
        response.message = `Removed ${n} word(s) from database`;
        return response;
    }
}
