import { Request } from "express";

export interface CreateWord extends Request {
    body: {
        korean: string, // Requires encodeURIComponent() on the korean word before sending
        english: string,
        level: string
    }
};

export interface ReadAllWords extends Request { };

export interface ReadWordById extends Request {
    params: {
        id: string
    }
};

export interface UpdateWordById extends Request {
    params: {
        id: string
    }
    body: {
        korean?: string,
        english?: string,
        level?: string
    }
};

export interface DeleteById extends Request {
    params: {
        id: string
    }
};