<script lang="ts">
  import AppSidebar from "@/components/nav/app-sidebar.svelte";
  import * as Breadcrumb from "$lib/components/ui/breadcrumb/index.js";
  import { Separator } from "$lib/components/ui/separator/index.js";
  import * as Sidebar from "$lib/components/ui/sidebar/index.js";
    import { m } from "@/paraglide/messages";
    import { APP_NAME, GITHUB_PROJECT_URL, APP_VERSION_NUMBER } from "@/config";
</script>
<Sidebar.Provider>
  <AppSidebar />
  <Sidebar.Inset>
    <header class="flex h-16 shrink-0 items-center gap-2">
      <div class="flex items-center gap-2 px-4">
        <Sidebar.Trigger class="-ml-1" />
      </div>
    </header>
    <div class="p-4 pt-0">
      <slot />
    </div>

    <footer class="flex flex-col justify-center items-center m-2">
      <a class="opacity-50 text-sm hover:underline" target="_blank" href={GITHUB_PROJECT_URL}>{m.powered_by_app({app_name: APP_NAME, version_number: APP_VERSION_NUMBER})}</a>
    </footer>
  </Sidebar.Inset>
</Sidebar.Provider>