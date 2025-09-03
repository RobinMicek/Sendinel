export function getLocalFormatedDate(isoDate: string = "") {
    if (isoDate == null || isoDate == undefined || isoDate.length == 0) return ""

    const localDate = new Date(isoDate);

    const formatted = localDate.toLocaleString(undefined, {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false // set to true for 12h format
        // timeZoneName: "short", // shows timezone like "GMT+3"
    });

    return formatted
}

