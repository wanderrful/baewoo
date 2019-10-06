import { Request } from "express";

export interface CreateUserRoute extends Request {
    body: {
        username: string,
        password: string
    }
}