<script lang="ts">
    import { LineChart } from "layerchart"; // Import Axis here
    import { scaleUtc } from "d3-scale";
    import { curveMonotoneX } from "d3-shape";
    import * as Chart from "$lib/components/ui/chart/index.js";
    import * as Card from "$lib/components/ui/card/index.js";

    export let data: Record<string, number> = {};
    export let label: string = "Line Chart";
    export let description: string = "Showing data over time";
    export let seriesLabel: string = "Value";
    export let seriesColor: string = "var(--chart-1)";

    // Convert { "YYYY-MM-DD": number } to [{ date: Date, value: number }]
    $: chartData = Object.entries(data)
        .map(([key, value]) => ({ date: new Date(key), value: value }))
        .sort((a, b) => a.date.getTime() - b.date.getTime());

    const chartConfig: Chart.ChartConfig = {
        value: { label: seriesLabel, color: seriesColor },
    };
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{label}</Card.Title>
        <Card.Description>{description}</Card.Description>
    </Card.Header>
    <Card.Content>
        <Chart.Container config={chartConfig} class="max-h-[400px] w-full p-4">
        <LineChart
        data={chartData}
        x="date"
        xScale={scaleUtc()}
        axis="x"
        series={[
          {
            key: "value",
            label: chartConfig.value.label,
            color: chartConfig.value.color,
          },
        ]}
        props={{
          spline: { curve: curveMonotoneX, strokeWidth: 3 },
          xAxis: {
            format: (v: Date) => v.toLocaleDateString(),
            ticks: Math.round(window.screen.availWidth / 200) // cca 200px per label,
        },
          highlight: { points: { r: 6 } },
        }}
      >
        {#snippet tooltip()}
            <Chart.Tooltip />
        {/snippet}
      </LineChart>
        </Chart.Container>
    </Card.Content>
</Card.Root>