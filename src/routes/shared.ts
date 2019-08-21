import { Response } from "express";
import { BaewooResponse } from "../models/dto/Response";

export const handleError = (res: Response, e: Error) => {
    const content = new BaewooResponse({
        data: {},
        message: e.message
    })

    res.status(400);
    res.send(content.render());
    res.end();
}