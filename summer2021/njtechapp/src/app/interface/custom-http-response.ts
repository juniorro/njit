import { ClassInfo } from "./classinfo";
import { Registration } from "./registration";
import { Student } from "./student";

export interface CustomHttpResponse {
    timeStamp: Date;
    statusCode: number;
    status: string;
    reason: string;
    message: string;
    developerMessage: string;
    students: Student[];
    registrations:  Registration[];
    classinfo: ClassInfo[];
}