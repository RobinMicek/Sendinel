<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Avatar from "../ui/avatar/index.js";
    import type { UserBasicsResponse } from "@/types/dtos/user";
    import { getLocalFormatedDate } from "@/utils/date-util";
    import { m } from "@/paraglide/messages";
    import { appSettingsStore } from "@/stores/store-factory";
    import { getGravatarUrl } from "@/utils/gravatar-util";
    import Button from "../ui/button/button.svelte";

    export let createdBy: UserBasicsResponse
    export let createdOn: string
    export let updatedBy: UserBasicsResponse
    export let updatedOn: string
</script>

<div class="grid sm:grid-cols-2 gap-6">
    <Card.Root>
        <Card.Content>
            <p class="text-lg font-semibold">{m.last_update()}</p>

            <div class="rounded-lg flex items-center justify-between">
                <div class="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                    <Avatar.Root class="size-16 rounded-lg">
                        {#if appSettingsStore.get()?.useGravatar && updatedBy?.email}
                            <Avatar.Image src={getGravatarUrl(updatedBy.email, 200)} alt={updatedBy?.firstname + " " + updatedBy?.lastname} />
                        {/if}
                        <Avatar.Fallback class="rounded-lg">{updatedBy?.firstname?.charAt(0) + updatedBy?.lastname?.charAt(0)}</Avatar.Fallback>
                    </Avatar.Root>
                    <div class="flex flex-col +text-left leading-tight">
                        <span class="truncate text-lg font-semibold">{updatedBy? `${updatedBy.firstname ?? ""} ${updatedBy.lastname ?? ""}`.trim() : m.unknown_user()}</span>

                        <span class="truncate text-md">{getLocalFormatedDate(updatedOn)}</span>
                        
                    </div>
                </div>

                {#if updatedBy?.id}
                    <Button href="/dashboard/user/{updatedBy.id}" target="_blank">Visit Profile</Button>
                {/if}
            </div>
        </Card.Content>
    </Card.Root>

    <Card.Root>
        <Card.Content>
            <p class="text-lg font-semibold">{m.created_by()}</p>

            <div class="rounded-lg flex items-center justify-between">
                
                <div class="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                    <Avatar.Root class="size-16 rounded-lg">
                        {#if appSettingsStore.get()?.useGravatar && createdBy?.email}
                            <Avatar.Image src={getGravatarUrl(createdBy.email, 200)} alt={createdBy?.firstname + " " + createdBy?.lastname} />
                        {/if}
                        <Avatar.Fallback class="rounded-lg">{createdBy?.firstname?.charAt(0) + createdBy?.lastname?.charAt(0)}</Avatar.Fallback>
                    </Avatar.Root>
                    <div class="flex flex-col +text-left leading-tight">
                        <span class="truncate text-lg font-semibold">{createdBy? `${createdBy.firstname ?? ""} ${createdBy.lastname ?? ""}`.trim() : m.unknown_user()}</span>
                        <span class="truncate text-md">{getLocalFormatedDate(createdOn)}</span>
                        
                    </div>
                </div>

                {#if createdBy?.id}
                    <Button href="/dashboard/user/{createdBy.id}" target="_blank">Visit Profile</Button>
                {/if}
            </div>
        </Card.Content>
    </Card.Root>
</div>