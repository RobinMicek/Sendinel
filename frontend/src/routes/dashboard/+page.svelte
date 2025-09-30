<script lang="ts">
    import * as Card from "@/components/ui/card/index.js";
    import { m } from "@/paraglide/messages";
    import Skeleton from "@/components/ui/skeleton/skeleton.svelte";
    import { triggerAlert } from "@/stores/alert-store";
    import { onMount } from "svelte";
    import StatsService from "@/services/stats-service";
    import type { StatsRequest, StatsResponse } from "@/types/dtos/stats";
    import RadialPercentageChart from "@/components/charts/radial-percentage-chart.svelte";
    import LineChart from "@/components/charts/line-date-chart.svelte";
    import DateRangeInput from "@/components/calendar/date-range-input.svelte";
    
    const statsService = new StatsService();

    const statsRequest: StatsRequest = {
        startDate: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000), // 1 week offset
        endDate: new Date()
    }

    let isLoading = false
    let statsData: StatsResponse

    async function getData(statsRequest: StatsRequest) {
        isLoading = true
        try {
            const response = await statsService.get(statsRequest);
            statsData = response

        } catch (e) {
            triggerAlert(m.failed_to_load_stats_data(), "", "error")
        } finally {
            isLoading = false
        }
    }

    onMount(async () => {
        await getData(statsRequest)
    })

    $: if (statsRequest.startDate && statsRequest.endDate) {
        getData(statsRequest);
    }
</script>



<div class="flex flex-1 flex-col gap-4 p-4 pt-0">
    <DateRangeInput bind:start={statsRequest.startDate} bind:end={statsRequest.endDate} />

    {#if !statsData || isLoading}
        <div class="grid md:grid-cols-2 gap-4">
            <Skeleton class="h-24" />
            <Skeleton class="h-24" />            
        </div>


        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-4">    
            <Skeleton class="aspect-video" />
            <Skeleton class="aspect-video" />
            <Skeleton class="aspect-video" />
        </div>

        <div>
            <Skeleton class="h-128" />
        </div>

    {:else}
        <div class="grid md:grid-cols-2 gap-4">
            <Card.Root>
                    <Card.Header>
                        <Card.Title>{m.total_sent()}</Card.Title>
                        <Card.Description>{m.number_of_sent_emails()}</Card.Description>
                        <Card.Action class="font-semibold text-2xl">{statsData.totalSent}</Card.Action>
                    </Card.Header>                        
                </Card.Root>

                <Card.Root>
                    <Card.Header>
                        <Card.Title>{m.unique_recipients()}</Card.Title>
                        <Card.Description>{m.unique_email_adresses()}</Card.Description>
                        <Card.Action class="font-semibold text-2xl">{statsData.uniqueRecipients}</Card.Action>
                    </Card.Header>                        
                </Card.Root>
        </div>


        <div class="grid md:grid-cols-2 xl:grid-cols-3 gap-4">    
            <RadialPercentageChart value={Math.round(statsData.bouncedRate)} label={m.bounced_rate()} description={m.emails_that_could_not_be_delivered()} mainColor="var(--chart-3)" placeholderColor="" />
            <RadialPercentageChart value={Math.round(statsData.failedRate)} label={m.failed_rate()} description={m.emails_that_could_not_be_sent()} mainColor="var(--chart-5)" placeholderColor="" />
            <RadialPercentageChart value={Math.round(statsData.openRate)} label={m.opened_rate()} description={m.emails_that_have_been_opened()} mainColor="var(--chart-2)" placeholderColor="" />
        </div>

        <LineChart data={statsData.totalSentDaily} label={m.sent_emails()} description={m.overview_of_sent_emails_each_day()} seriesLabel={m.emails()} seriesColor="var(--chart-2)" />
    {/if}
</div>