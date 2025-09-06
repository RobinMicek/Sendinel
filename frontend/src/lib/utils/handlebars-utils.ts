import Handlebars from "handlebars";

export function renderHandlebarsTemplate<T>(templateSource: string, context: T): string {
    const template = Handlebars.compile(templateSource);
    return template(context);
}