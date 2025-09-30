<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import * as Select from "@/components/ui/select/index.js";
    import * as Table from "@/components/ui/table/index.js";
    import Button from "@/components/ui/button/button.svelte";
    import Input from "@/components/ui/input/input.svelte";
    import Label from "@/components/ui/label/label.svelte";
    import ReturnBack from "@/components/return-back/return-back.svelte";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { getLocalFormatedDate } from "@/utils/date-util";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";
    import EmailService from "@/services/email-service";
    import type { EmailResponse } from "@/types/dtos/email";
    import { emailStatusesMeta } from "@/types/enums/email-statuses-enum";
    import { JSONEditor } from "svelte-jsoneditor";
    import DatatableBadgeColored from "@/components/datatable/datatable-badge-colored.svelte";
    import { emailPrioritiesMeta } from "@/types/enums/email-priorities-enum";
    import { mode } from "mode-watcher";
    import DatatableTruncatedText from "@/components/datatable/datatable-truncated-text.svelte";

    export let data: { id: string }

    const emailService = new EmailService()

    let isLoading = false
    let isExportToPdfButtonLoading = false
    let emailData: EmailResponse

    async function handleExportToPdf(id: string) {
        isExportToPdfButtonLoading = true
        try {
            const response = await emailService.exportToPdf(id)

            const fileUrl = window.URL.createObjectURL(response.blob)
            const a = document.createElement("a")
            a.href = fileUrl
            a.download = response.filename

            document.body.appendChild(a)
            a.click()

            a.remove()
            window.URL.revokeObjectURL(fileUrl)
        } catch (e) {
            triggerAlert(m.failed_to_render_pdf(), "", "error")
        } finally {
            isExportToPdfButtonLoading = false;
        }
    }

    async function getData(id: string) {
        isLoading = true
        try {
            const response = await emailService.get(id);
            emailData = response
        } catch (e) {
            triggerAlert(m.failed_to_get_email(), "", "error")    
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(data.id)
    })
</script>

<div class="flex justify-between mb-6">
    <ReturnBack backUrl="/dashboard/email" />

    <Button disabled={isExportToPdfButtonLoading} onclick={() => {handleExportToPdf(data.id)}}>Export to PDF</Button>
</div>

<form class="flex flex-col gap-6">
    {#if isLoading || !emailData}
        <div class="grid md:grid-cols-3 gap-6">
            <Skeleton class="h-24" />
            <Skeleton class="h-24" />
            <Skeleton class="h-24" />
        </div>


        <Skeleton class="h-64" />
        <Skeleton class="h-64" />
        <Skeleton class="h-64" />

    {:else}
         <div class="grid md:grid-cols-3 gap-6">
            <Card.Root>
                <Card.Header>
                    <Card.Title>
                        <a class="hover:underline" href="/dashboard/template/{emailData.template.id}">
                            {emailData.template.name}
                        </a>
                    </Card.Title>
                    <Card.Description>{emailData.template.description}</Card.Description>
                </Card.Header>            
            </Card.Root>

            <Card.Root>
                <Card.Header>
                    <Card.Title>
                        <a class="hover:underline" href="/dashboard/sender/{emailData.sentBy.id}">
                            {emailData.sentBy.name}
                        </a>
                    </Card.Title>
                    <Card.Description>{emailData.sentBy.description}</Card.Description>
                </Card.Header>            
            </Card.Root>

            <Card.Root>
                <Card.Header>
                    <Card.Title>
                        <a class="hover:underline" href="/dashboard/client/{emailData.requestedBy.id}">
                            {emailData.requestedBy.name}
                        </a>
                    </Card.Title>
                    <Card.Description>{emailData.requestedBy.description}</Card.Description>
                </Card.Header>            
            </Card.Root>
        </div>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.email_information()}</Card.Title>
                <Card.Description>{m.basic_information_about_this_email()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <div class="grid md:grid-cols-3 gap-6 w-full">
                    <div class="flex flex-col items-start gap-2">
                        <Label for="to_address">{m.recipient()}</Label>
                        <Input id="to_address" type="text" required readonly value={emailData.toAddress} />                        
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="role">{m.priority()}</Label>
                        <Select.Root                
                            type="single"
                            disabled
                            value={emailPrioritiesMeta[emailData.priority].translation}
                        >
                            <Select.Trigger class="w-full hover:cursor-pointer" disabled>{emailPrioritiesMeta[emailData.priority].translation}</Select.Trigger>                            
                        </Select.Root>
                    </div>

                    <div class="flex flex-col items-start gap-2">
                        <Label for="created_on">{m.created_on()}</Label>
                        <Input id="created_on" type="text" required readonly value={getLocalFormatedDate(emailData.createdOn)} />
                    </div>
                </div>
            </Card.Content>
        </Card.Root>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.history()}</Card.Title>
                <Card.Description>{m.how_the_email_was_handled_so_far()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <Table.Root>
                    <Table.Header>
                        <Table.Row>
                            <Table.Head>{m.status()}</Table.Head>
                            <Table.Head>{m.note()}</Table.Head>                        
                            <Table.Head class="text-right">{m.created_on()}</Table.Head>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {#each emailData.emailStatuses.reverse() as emailStatus}
                            <Table.Row>
                                <Table.Cell class="font-medium">
                                    <DatatableBadgeColored text={emailStatusesMeta[emailStatus.status].translation} color={emailStatusesMeta[emailStatus.status].color} icon={emailStatusesMeta[emailStatus.status].icon} />
                                </Table.Cell>
                                <Table.Cell>
                                    <DatatableTruncatedText text={emailStatus.note} />
                                </Table.Cell>
                                <Table.Cell class="text-right">{getLocalFormatedDate(emailStatus.createdOn)}</Table.Cell>
                            </Table.Row>
                        {/each}
                    </Table.Body>
                </Table.Root>
            </Card.Content>
        </Card.Root>

        <Card.Root>
            <Card.Header>
                <Card.Title>{m.template_variables()}</Card.Title>
                <Card.Description>{m.data_specific_to_this_email_used_to_personalize_content_across_the_template()}</Card.Description>
            </Card.Header>
            <Card.Content>
                <div class={mode.current != "light" ? "jse-theme-dark" : ""}>
                    <JSONEditor
                        content={{ json: emailData.templateVariables }}
                        readOnly={true}
                        mainMenuBar={false}
                        navigationBar={false}
                        statusBar={true}
                        onRenderMenu={() => []}
                        onRenderContextMenu={() => false}
                    />
                </div>

                <style>
                    @import 'svelte-jsoneditor/themes/jse-theme-dark.css';
                </style>
            </Card.Content>
        </Card.Root>

    {/if}
</form>