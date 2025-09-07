<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { Label } from "$lib/components/ui/label/index.js";
    import { Switch } from "$lib/components/ui/switch/index.js";
    import { FileDropZone, type FileDropZoneProps } from "$lib/components/ui/file-drop-zone";
    import { APP_NAME } from "@/config";
    import { m } from "@/paraglide/messages";
    import TemplateService from "@/services/template-service";
    import { triggerAlert } from "@/stores/alert-store";
    import type { TemplateImportRequest } from "@/types/dtos/template";
    import Progress from "@/components/ui/progress/progress.svelte";
    import { CloudUpload, Upload } from "@lucide/svelte";

    let isLoading = false
    let uploadPercentage = 0
    let templateImportRequest: TemplateImportRequest = {
        overwriteExisting: false
    }

    const templateService = new TemplateService()
    
    async function handleImport(templateImportRequest: TemplateImportRequest, file: File) {
        isLoading = true
        try {
            const response = await templateService.import(templateImportRequest, file, (percentage: number) => {uploadPercentage = percentage})
            
            triggerAlert(m.templates_successfully_imported(), "", "success")
        } catch (e) {
            triggerAlert(m.failed_to_import_templates(), "", "error")
        } finally {
            isLoading = false
        }
    }

    const onUpload: FileDropZoneProps['onUpload'] = async (files) => {
        if (!files || files.length === 0) return;

        isLoading = true;
        try {
            const file = files[0]; // maxFiles=1 ensures only one file

            await handleImport(templateImportRequest, file)

        } catch (error: any) {
            console.error("Import failed", error);
            triggerAlert(m.failed_to_upload_file({ filename: files[0].name }), error?.message || "", "error");
        } finally {
            isLoading = false;
        }
    };

    const onFileRejected: FileDropZoneProps['onFileRejected'] = async ({ reason, file }) => {
		triggerAlert(m.failed_to_upload_file({filename: file.name}), reason, "error")
	};
</script>


<Card.Root>
    <Card.Header>
        <Card.Title>{m.template_import()}</Card.Title>
        <Card.Description>{m.import_templates_from_backup_or_other_instance_of_app({app_name: APP_NAME})}</Card.Description>
    </Card.Header>
    <Card.Content>
         <div class="flex gap-3 mb-4">
            <Switch class="hover:cursor-pointer" id="overwrite-existing-templates" bind:checked={templateImportRequest.overwriteExisting} />
            <Label for="overwrite-existing-templates">{m.overwrite_existing_templates()}</Label>                    
        </div>

        <div class="flex flex-col justify-center items-center gap-6 w-full">
            <FileDropZone
                {onUpload}
                {onFileRejected}
                disabled={isLoading}
            >

                <div class="flex flex-col justify-center items-center gap-2">
                    <CloudUpload stroke-width={3} size="64" />
                    {m.drag_and_drop_your_templates_export()}
                </div>
            </FileDropZone>

            {#if isLoading}
                <Progress value={uploadPercentage} max={100} class="w-[60%]" />
            {/if}
        </div>
    </Card.Content>
</Card.Root>