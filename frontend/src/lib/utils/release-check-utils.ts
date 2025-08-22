import { APP_VERSION_NUMBER, GITHUB_API_LATEST_RELEASE_URL, GITHUB_LATEST_RELEASE_URL } from "@/config";

export default async function getLatestRelease(): Promise<LatestRelease> {
    const response = await fetch(GITHUB_API_LATEST_RELEASE_URL);
    const data = await response.json();

    return {
        version: data.tag_name,
        url: GITHUB_LATEST_RELEASE_URL,
        hidden: false,
        new: data.tag_name != APP_VERSION_NUMBER
    }
}

getLatestRelease();