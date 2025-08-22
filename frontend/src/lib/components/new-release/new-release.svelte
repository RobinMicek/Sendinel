<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog/index.js";
    import { APP_NAME, APP_VERSION_NUMBER, GITHUB_API_LATEST_RELEASE_URL } from "@/config";
    import { m } from "@/paraglide/messages";
    import { latestReleaseStore } from "@/stores/store-factory";
    import getLatestRelease from "@/utils/release-check-utils";
    import { onMount } from "svelte";

    let isOpen = true

    onMount(async () => {
        if (latestReleaseStore.get() == null) {
            const response = await getLatestRelease();

            latestReleaseStore.set(response);
        }
    })
</script>

{#if $latestReleaseStore && $latestReleaseStore.version && !$latestReleaseStore.hidden && $latestReleaseStore.new} 
    <AlertDialog.Root open={isOpen}>
        <AlertDialog.Content>
            <AlertDialog.Header>
                <AlertDialog.Title>
                    {m.new_version_available()}
                </AlertDialog.Title>
                <AlertDialog.Description>
                    {m.new_version_is_available({new_version_number: $latestReleaseStore.version, old_version_number: APP_VERSION_NUMBER, app_name: APP_NAME, show_button_name: m.show()})}
                </AlertDialog.Description>
            </AlertDialog.Header>
            <AlertDialog.Footer>
                <AlertDialog.Cancel
                    class="hover: cursor-pointer"
                    onclick={() => {
                        const info = $latestReleaseStore;
                        if (!info) return;
                        latestReleaseStore.set({ ...info, hidden: true })

                        isOpen = false
                    }}
                >
                    {m.close()}
                </AlertDialog.Cancel>
                
                <a target="_blank" href={GITHUB_API_LATEST_RELEASE_URL}>
                    <AlertDialog.Action class="hover: cursor-pointer">
                        {m.show()}
                    </AlertDialog.Action>
                </a>
            </AlertDialog.Footer>
        </AlertDialog.Content>
    </AlertDialog.Root>
{/if}
