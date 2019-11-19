/* This file defines routes for the prefix `/api/v1/accounts/` */
import { Router } from "express";

import { CreateUserRoute } from "routes/routes.interface";

const AccountsRouter = Router();

AccountsRouter.get("/", (req, res) => {
    res.send("You have reached the Accounts API route root. Please leave a message after the beep. *beep*");
    res.end();
})

AccountsRouter.get("/user", (req, res) => {
    res.json({});
    res.end();
});

AccountsRouter.post("/user", (req: CreateUserRoute, res) => {
    const { username, password } = req.body;
    res.send(`TO DO: I want to make an account for ${username} with a password of ${password}`);
    res.end();
});

export default AccountsRouter;