<script lang="ts">
    import TabExport from "./tabs/tab-export.svelte";
    import TabImport from "./tabs/tab-import.svelte";
    import { appSettingsStore, userStore } from "@/stores/store-factory";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";

    const importEnabled = appSettingsStore.get()?.allowTemplateImports
    const canImport = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.TEMPLATES_CREATE)
    const canExport = userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.TEMPLATES_READ)
</script>

<div class="flex flex-col w-full gap-6">
    {#if canImport && importEnabled}
        <TabImport />
    {/if}

    {#if canExport}
        <TabExport />
    {/if}
</div>