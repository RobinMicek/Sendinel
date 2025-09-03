<script lang="ts">
    import Calendar from "@/components/ui/calendar/calendar.svelte";
    import * as Popover from "@/components/ui/popover/index.js";
    import { Button } from "@/components/ui/button";
    import ChevronDownIcon from "@lucide/svelte/icons/chevron-down";
    import { getLocalTimeZone, type CalendarDate, parseDate, toLocalTimeZone } from "@internationalized/date";
        import { m } from "@/paraglide/messages";

    // props
    export let id: string;
    export let maxValue: CalendarDate | undefined = undefined;

    // state
    let open = false;

    // exposed value (string in yyyy-mm-dd format)
    export let value: string | undefined = undefined;

    // internal calendar state (CalendarDate)
    let internalValue: CalendarDate | undefined = value
        ? parseDate(value) // prefill if parent passes string
        : undefined;

    // whenever internal changes, sync string value
    $: if (internalValue) {
        const d = internalValue.toDate(getLocalTimeZone());
        value = d.toISOString().slice(0, 10); // yyyy-mm-dd
    } else {
        value = undefined;
    }
</script>

<div class="flex flex-col gap-3 w-full">
    <Popover.Root bind:open>
        <Popover.Trigger id={id}>
            {#snippet child({ props })}
                <Button
                    {...props}
                    variant="outline"
                    class="w-full justify-between font-normal"
                    >
                    {#if value}
                        {internalValue?.toDate(getLocalTimeZone()).toLocaleDateString()}
                    {:else}
                        {m.select_date()}
                    {/if}
                    <ChevronDownIcon />
                </Button>
            {/snippet}
        </Popover.Trigger>

        <Popover.Content class="w-auto overflow-hidden p-0" align="start">
            <Calendar
                type="single"
                bind:value={internalValue}
                captionLayout="dropdown"
                onValueChange={() => (open = false)}
                {maxValue}
            />
        </Popover.Content>
    </Popover.Root>
</div>
