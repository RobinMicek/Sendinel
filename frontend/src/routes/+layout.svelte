<script lang="ts">
	import '../app.css';
	import faviconPng96 from "@/assets/images/logo/favicon/favicon-96x96.png";
	import faviconSvg from "@/assets/images/logo/favicon/favicon.svg";
	import faviconIco from "@/assets/images/logo/favicon/favicon.ico";
	import faviconPngApple from "@/assets/images/logo/favicon/apple-touch-icon.png";
	import faviconManifest from "@/assets/images/logo/favicon/site.webmanifest";
	import Alert from "@/components/alert/Alert.svelte"
	import { ModeWatcher } from "mode-watcher";
    import { onMount } from 'svelte';
    import { oobeStatusStore, tokenStore, userStore } from '@/stores/auth-store';
    import { goto } from '$app/navigation';
    import AuthService from '@/services/auth-service';
    import { triggerAlert } from '@/stores/alert-store';
    import { m } from '@/paraglide/messages';
    import { appSettingsStore } from '@/stores/app-settings-store';
    import AppSettingsService from '@/services/app-settings-service';

	const authService = new AuthService()
	const appSettingsService = new AppSettingsService()

	onMount(async () => {
		// Cache oobe status
		if (oobeStatusStore.get() == null) {
			try {
				const response = await authService.oobeStatus()
				oobeStatusStore.set(response.oobeStatus)
			} catch (e) {
				triggerAlert(m.failed_to_get_oobe_status(), "", "error")
				return
			}
		}

		// Redirect to oobe if not completed
		if (oobeStatusStore.get()) {
			goto("/auth/oobe")
			return
		}

		// Redirect to login if missing token
		if (tokenStore.get() == null || userStore.get() == null) {
			goto("/auth")
			return
		}

		// Cache app settings
		// Only do this after successful auth
		if (appSettingsStore.get() == null) {
			try {
				const response = await appSettingsService.getSettings()
				appSettingsStore.set(response)
			} catch (e) {
				triggerAlert(m.failed_to_get_app_settings(), "", "error")
			}
		}

		// TODO: Verify if JWT expired
		// TODO: Log redirect for after auth
		// TODO: Only redirect if not on dashboard url
		goto("/dashboard")
	})
</script>

<svelte:head>
	<link rel="icon" type="image/png" href={faviconPng96} sizes="96x96" />
	<link rel="icon" type="image/svg+xml" href={faviconSvg} />
	<link rel="shortcut icon" href={faviconIco} />
	<link rel="apple-touch-icon" sizes="180x180" href={faviconPngApple} />
	<link rel="manifest" href={faviconManifest} />

	<title>Sendinel</title>
</svelte:head>

<ModeWatcher />
<Alert />
<slot />
