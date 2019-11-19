export class Response {
    constructor(public data: object = {}, public message: string = "", public statusCode: number = 200) { }
}