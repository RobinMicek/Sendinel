<script lang="ts">
	import '../app.css';
	import faviconPng96 from "@/assets/images/logo/favicon/favicon-96x96.png";
	import faviconSvg from "@/assets/images/logo/favicon/favicon.svg";
	import faviconIco from "@/assets/images/logo/favicon/favicon.ico";
	import faviconPngApple from "@/assets/images/logo/favicon/apple-touch-icon.png";
	import faviconManifest from "@/assets/images/logo/favicon/site.webmanifest";
	import Alert from "@/components/alert/Alert.svelte";
	import { ModeWatcher } from "mode-watcher";
    import { APP_NAME } from '@/config';
</script>

<svelte:head>
	<link rel="icon" type="image/png" href={faviconPng96} sizes="96x96" />
	<link rel="icon" type="image/svg+xml" href={faviconSvg} />
	<link rel="shortcut icon" href={faviconIco} />
	<link rel="apple-touch-icon" sizes="180x180" href={faviconPngApple} />
	<link rel="manifest" href={faviconManifest} />

	<title>{APP_NAME}</title>
</svelte:head>

<slot />
<ModeWatcher />
<Alert />
