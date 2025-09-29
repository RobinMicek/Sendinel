<script lang="ts">
  import * as Card from "$lib/components/ui/card/index.js";
  import * as Chart from "$lib/components/ui/chart/index.js";
  import { PieChart, Text } from "layerchart";

  export let value: number = 29
  export let label: string = "Progress"
  export let description: string = "Current progress"
  export let mainColor: string = "var(--chart-1)"
  export let placeholderColor: string = "var(--muted-foreground)"

  // ShadCN chart config placeholder
  const chartConfig: Chart.ChartConfig = {
    value: { label: label, color: mainColor },
    placeholder: { label: "Remaining", color: placeholderColor }
  }

  $: safeValue = Math.min(Math.max(value, 0), 100)

  $: pieData = [
    { percentage: " ", value: safeValue, color: mainColor },
    { percentage: "", value: 100 - safeValue, color: placeholderColor },
  ];
</script>

<Card.Root class="flex flex-col">
    <Card.Header class="items-center">
        <Card.Title>{label}</Card.Title>
        <Card.Description>{description}</Card.Description>
    </Card.Header>

    <Card.Content class="flex-1">
        <Chart.Container config={chartConfig} class="mt-4">
            <PieChart
                data={pieData}
                key="percentage"
                value="value"
                c="color"
                innerRadius={80}
                range={[-90, 90]}
                props={{ pie: { sort: null } }}
                cornerRadius={4}
            >
                {#snippet aboveMarks()}
                    <Text
                        value={`${safeValue}%`}
                        textAnchor="middle"
                        verticalAnchor="middle"
                        class="fill-foreground text-2xl! font-bold"
                        dy={-24}
                    />
                    <Text
                        value={label}
                        textAnchor="middle"
                        verticalAnchor="middle"
                        class="fill-muted-foreground! text-muted-foreground"
                        dy={4}
                    />
                {/snippet}        
            </PieChart>
        </Chart.Container>
    </Card.Content>
</Card.Root>
