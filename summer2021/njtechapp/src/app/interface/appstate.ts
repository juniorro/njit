import { DataState } from "../enum/datastate.enum";

export interface AppState<T> {
    dataState: DataState;
    data?: T;
    error?: string;
}