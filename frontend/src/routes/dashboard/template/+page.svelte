<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
    import Checkbox from "@/components/ui/checkbox/checkbox.svelte";
    import { Button } from "@/components/ui/button/index.js";
    import { m } from "@/paraglide/messages";    
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import DataTable from "@/components/datatable/datatable.svelte";
    import { columns } from "./columns.js";
    import type { PageResponse } from "@/types/dtos/page";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";
    import DatatableNav from "@/components/datatable/datatable-nav.svelte"
    import { userStore } from "@/stores/store-factory";
    import { hasPermission } from "@/types/enums/user-roles-enum";
    import { UserPermissionsEnum } from "@/types/enums/user-permissions-enum";
    import { goto } from "$app/navigation";
    import TemplateService from "@/services/template-service.js";
    import type { TemplateResponse, TemplateTagResponse } from "@/types/dtos/template.js";
    import DatatableSearch from "@/components/datatable/datatable-search.svelte";
    import { Tags } from "@lucide/svelte";

    const templateService = new TemplateService();

    let isLoading = false
    let currentPageNumber: number
    let sortKey: string = "name"
    let sortOrder: "asc" | "desc" = "asc"
    let search: string = ""
    let pageData: PageResponse<TemplateResponse>
    let allTemplateTags: TemplateTagResponse[]
    let selectedTag: TemplateTagResponse | null = null

        
    async function getAllTemplateTags() {
        isLoading = true
        try {
            const response = await templateService.getAllTags()
            allTemplateTags = response
        } catch (e) {
            triggerAlert(m.failed_to_get_available_template_tags(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    async function getData(pageNumber: number, sortKey: string, sortOrder: "asc" | "desc", searchString: string = "", selectedTag: TemplateTagResponse | null) {
        isLoading = true
        try {
            const response = await templateService.getAll(pageNumber, undefined, sortKey, sortOrder, searchString, selectedTag);
            pageData = response
            currentPageNumber = response.pageNumber

        } catch (e) {
            triggerAlert(m.failed_to_get_templates(), "", "error")
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(currentPageNumber, sortKey, sortOrder, "", null)
        await getAllTemplateTags()
    })
</script>

<div class="w-full flex flex-col gap-6">    
    <Card.Root class="w-full">
    <Card.Header>
        <Card.Title>{m.templates()}</Card.Title>
        <Card.Description>{m.define_how_your_emails_look()}</Card.Description>
        <Card.Action>
            {#if userStore.get()?.role && hasPermission(userStore.get()!.role, UserPermissionsEnum.TEMPLATES_CREATE)}
                <Button onclick={() => {goto("/dashboard/template/new")}}>{m.new_template()}</Button>
            {/if}
        </Card.Action>
    </Card.Header>  
    </Card.Root>

    {#if !pageData || !allTemplateTags || isLoading}
        <Skeleton class="aspect-video" />

    {:else}    
        <div class="flex gap-2">            
            {#if allTemplateTags.length > 0}
                <Popover>
                    <PopoverTrigger>
                        <Button variant="outline">
                            <Tags />
                        </Button>
                    </PopoverTrigger>

                    <PopoverContent>
                        <div class="flex flex-col gap-1 max-h-128 overflow-y-scroll">
                            {#each allTemplateTags as tag}
                                <label class="flex items-center gap-4 cursor-pointer w-full gap-2">
                                    <Checkbox
                                        checked={selectedTag?.name === tag.name}
                                        onclick={async () => {                    
                                            if (selectedTag?.name === tag.name) {
                                                selectedTag = null                                
                                            } else {
                                                selectedTag = tag
                                            }

                                            await getData(1, sortKey, sortOrder, search, selectedTag);
                                        }}
                                    />
                                    <span>{tag.name}</span>
                                </label>
                            {/each}
                        </div>  
                    </PopoverContent>
                </Popover>                
            {/if}

            <DatatableSearch bind:searchString={search} getData={searchString => getData(1, sortKey, sortOrder, searchString, selectedTag)} />
        </div>

        <DataTable
            data={pageData.content!}
            columns={columns}
            sortKey={sortKey}
            sortOrder={sortOrder}
            onSort={(key, nextOrder) => {
                sortKey = key;
                sortOrder = nextOrder;
                getData(1, sortKey, sortOrder, search, selectedTag);
            }}
        />
        <DatatableNav
            pageData={pageData}
            currentPageNumber={currentPageNumber}
            getData={pageNumber => getData(pageNumber, sortKey, sortOrder, search, selectedTag)}
        />
    {/if}

</div>