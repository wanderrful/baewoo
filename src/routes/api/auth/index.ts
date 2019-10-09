import { authenticate } from "passport";
import { Router } from "express";

const AuthRouter = Router();

// Turns out Twitch needs a phone number to register an app for its OAuth2 services...
AuthRouter.get("/twitch", authenticate("twitch"));
AuthRouter.get("/twitch/redirect", authenticate("twitch", { failureRedirect: "/" }), (req, res) => {
    // Successful authentication, redirect home.
    res.redirect("/");
});

export default AuthRouter;