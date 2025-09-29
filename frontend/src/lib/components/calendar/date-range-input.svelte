<script lang="ts">
    import CalendarIcon from "@lucide/svelte/icons/calendar";
    import type { DateRange } from "bits-ui";
    import { CalendarDate, DateFormatter, type DateValue, getLocalTimeZone, today } from "@internationalized/date";
    import { cn } from "$lib/utils.js";
    import { buttonVariants } from "$lib/components/ui/button/index.js";
    import { RangeCalendar } from "$lib/components/ui/range-calendar/index.js";
    import * as Popover from "$lib/components/ui/popover/index.js";
    import { m } from "@/paraglide/messages";
    
    const df = new DateFormatter(navigator.language || "en-US", {
        dateStyle: "long"
    });
    
    const currentDate = new Date(Date.now())

    export let value: DateRange = {
        start: new CalendarDate(currentDate.getFullYear(), currentDate.getMonth() + 1, currentDate.getDate()).subtract({ days: 7 }),
        end: new CalendarDate(currentDate.getFullYear(), currentDate.getMonth() + 1, currentDate.getDate())
    };

    // Derived start and end for convenience
    export let start: Date | undefined;
    export let end: Date | undefined;
    $: start = value?.start? new Date(value!.start!.toString()) : undefined;
    $: end = value?.end? new Date(value!.end!.toString()) : undefined;

    let startValue: DateValue | undefined = undefined;
</script>

<div class="grid gap-2">
    <Popover.Root>
        <Popover.Trigger
            class={cn(
                buttonVariants({ variant: "outline" }),
                !value && "text-muted-foreground"
            )}
        >
            <CalendarIcon class="mr-2 size-4" />

            {#if value && value.start}
                {#if value.end}
                    {df.format(value.start.toDate(getLocalTimeZone()))} - {df.format(value.end.toDate(getLocalTimeZone()))}                
                {:else}            
                    {df.format(value.start.toDate(getLocalTimeZone()))}
                {/if}

            {:else if startValue}
                {df.format(startValue.toDate(getLocalTimeZone()))}
            
            {:else}
                {m.select_date()}

            {/if}
        </Popover.Trigger>
        <Popover.Content class="w-auto p-0" align="start">
            <RangeCalendar
                bind:value
                onStartValueChange={(v) => {
                    startValue = v;        
                }}
                maxValue={today(getLocalTimeZone())}
                numberOfMonths={1}
            />
        </Popover.Content>
    </Popover.Root>
</div>