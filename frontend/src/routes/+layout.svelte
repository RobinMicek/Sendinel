<script lang="ts">
	import '../app.css';
	import favicon from '$lib/assets/favicon.svg';
	import Alert from "@/components/alert/Alert.svelte"
	import { ModeWatcher } from "mode-watcher";
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
	<title>Sendinel</title>
</svelte:head>

<ModeWatcher />
<Alert />
<slot />
