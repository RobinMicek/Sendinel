<script lang="ts">
    import * as Card from "@/components/ui/card";
    import appLogoDark from "@/assets/images/logo/logo-dark.png";
    import appLogoLight from "@/assets/images/logo/logo-light.png";
    import pictureDark from '@/assets/images/auth/auth-dark.png';
    import pictureLight from '@/assets/images/auth/auth-light.png';
    import githubLogoDark from "@/assets/images/icons/github/github-mark-dark.png";
    import githubLogoLight from "@/assets/images/icons/github/github-mark-light.png"
    import { mode } from "mode-watcher";
    import { APP_NAME, GITHUB_PROJECT_URL } from "@/config";
</script>

<div class="bg-background flex h-screen w-screen flex-col items-center justify-center">

    <div class="m-5 flex items-center gap-4">
        <img
            src={mode.current == "light" ? appLogoDark: appLogoLight}
            alt="App logo"
            class="h-16"
        />
        <h1 class="font-semibold text-4xl">{APP_NAME}</h1>
    </div>

    <Card.Root class="overflow-hidden p-0 w-3/5 2xl:w-1/2 h-2/3 2xl:h-3/5">
        <Card.Content class="grid p-0 md:grid-cols-2 h-full">

            <slot />

            <div class="bg-muted hidden md:block h-full w-full">
                <img
                        src={mode.current == "light" ? pictureLight : pictureDark}
                        alt="Man reading emails on his laptop"
                        class="h-full w-full object-cover inset-0"
                />
            </div>
        </Card.Content>
    </Card.Root>

    <div class="m-5">
        <a href="{GITHUB_PROJECT_URL}" target="_blank">
            <img
                src={mode.current == "light" ? githubLogoDark : githubLogoLight}
                alt="Github logo"
                class="h-8 hover:cursor-pointer"
            />
        </a>
    </div>
</div>

