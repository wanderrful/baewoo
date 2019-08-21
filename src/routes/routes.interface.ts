import { Request } from "express";

export interface UsernameRoute extends Request {
    params: {
        username: string,
        password: string
    }
}