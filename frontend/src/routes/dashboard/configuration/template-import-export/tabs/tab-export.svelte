<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Table from "@/components/ui/table/index.js";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { APP_NAME } from "@/config";
    import { m } from "@/paraglide/messages";
    import TemplateService from "@/services/template-service";
    import { triggerAlert } from "@/stores/alert-store";
    import type { TemplateBasicsResponse, TemplateExportRequest } from "@/types/dtos/template";
    import { onMount } from "svelte";
    import DatatableTruncatedText from "@/components/datatable/datatable-truncated-text.svelte";
    import Button from "@/components/ui/button/button.svelte";
    import { Eye } from "@lucide/svelte";
    import Checkbox from "@/components/ui/checkbox/checkbox.svelte";

    let isLoading = false
    let exportIds = new Set<string>

    const templateService = new TemplateService()
    let templateData: Array<TemplateBasicsResponse>

    async function handleExport(exportIds: Set<string>) {
        isLoading = true
        try {
            if (exportIds.size < 1) {
                triggerAlert(m.no_templates_have_been_selected(), "", "error")
                return
            }

            const templateExportRequest: TemplateExportRequest = {
                ids: Array.from(exportIds.values())
            }

            const response = await templateService.export(templateExportRequest);
            
            const fileUrl = window.URL.createObjectURL(response.blob)
            const a = document.createElement("a")
            a.href = fileUrl
            a.download = response.filename

            document.body.appendChild(a)
            a.click()

            a.remove()
            window.URL.revokeObjectURL(fileUrl)
        } catch (e) {
            triggerAlert(m.failed_to_export_templates(), "", "error")
        } finally {
            isLoading = false
        }
    }

    async function getData() {
        isLoading = true
        try {
            const response = await templateService.getAllBasics();
            templateData = response
        } catch (e) {
            triggerAlert(m.failed_to_get_templates(), "", "error")
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData()
    })
</script>

{#if isLoading || !templateData}
    <Skeleton class="h-64" />

{:else}
    <Card.Root>
        <Card.Header>
            <Card.Title>{m.template_export()}</Card.Title>
            <Card.Description>{m.export_template_to_back_them_up_or_move_to_a_different_instance_of_app({app_name: APP_NAME})}</Card.Description>
        </Card.Header>
        <Card.Content>
            <div class="overflow-y-auto max-h-[70vh] mt-5">
                <Table.Root>                    
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell>
                                <Checkbox class="hover:cursor-pointer" checked={exportIds.size === templateData.length} onCheckedChange={(checked) => {
                                        if (checked) {                                        
                                            exportIds = new Set(templateData.map(t => t.id))
                                        } else {
                                            exportIds = new Set()
                                        }                                        
                                    }}
                                />
                            </Table.Cell>
                            <Table.Cell></Table.Cell>
                            <Table.Cell></Table.Cell>
                            <Table.Cell></Table.Cell>
                        </Table.Row>
                        
                        {#each templateData as template}
                            <Table.Row>
                                <Table.Cell>
                                    <Checkbox class="hover:cursor-pointer" checked={exportIds.has(template.id)} onCheckedChange={(checked) => {
                                            if (checked) {
                                                exportIds = new Set([...exportIds, template.id]) // new Set for reactivity
                                            } else {
                                                const newSet = new Set(exportIds)
                                                newSet.delete(template.id)
                                                exportIds = newSet // reassign to trigger reactive updates
                                            }
                                        }} 
                                    />
                                </Table.Cell>
                                <Table.Cell class="font-medium">{template.name}</Table.Cell>
                                <Table.Cell class="truncate max-w-[100px]">
                                    <DatatableTruncatedText text={template.description} />
                                </Table.Cell>                            
                                <Table.Cell class="text-right">
                                    <a target="_blank" href="/dashboard/template/{template.id}">
                                        <Button variant="secondary">
                                            <Eye />
                                        </Button>
                                    </a>                                    
                                </Table.Cell>
                            </Table.Row>
                        {/each}
                    </Table.Body>
                </Table.Root>
            </div>

            <div class="flex justify-center w-full mt-2">
                <Button onclick={async () => {handleExport(exportIds)}}>
                    {m.download_export()}                    
                </Button>
            </div>
        </Card.Content>
    </Card.Root>
{/if}