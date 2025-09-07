<script lang="ts" module>
	import appLogo from "@/assets/images/logo/logo.png";
	import Mail from "@lucide/svelte/icons/mail";
	import NotebookText from "@lucide/svelte/icons/notebook-text";
	import Send from "@lucide/svelte/icons/send";
	import AppWindowMac from "@lucide/svelte/icons/app-window-mac";
	import User from "@lucide/svelte/icons/user";
	import Book from "@lucide/svelte/icons/book";
	import Wrench from "@lucide/svelte/icons/wrench";
	import type { UserResponse } from "@/types/dtos/user";
    import { APP_NAME, DOCUMENTATION_URL } from "@/config";

	let user: UserResponse | undefined | null = userStore.get();
	
	const data = {
		user: {
			firstname: user!.firstname,
			lastname: user!.lastname,
			email: user!.email
		},
		navMain: [
			{
				title: m.emails(),
				url: "/dashboard/email",
				icon: Mail
			},
			{
				title: m.templates(),
				url: "/dashboard/template",
				icon: NotebookText
			},
			{
				title: m.senders(),
				url: "/dashboard/sender",
				icon: Send
			},
			{
				title: m.clients(),
				url: "/dashboard/client",
				icon: AppWindowMac
			},
			{
				title: m.users(),
				url: "/dashboard/user",
				icon: User
			},
			{
				title: m.configuration(),
				icon: Wrench,
                url: "",
				items: [
					{
						title: m.application_settings(),
						url: "/dashboard/configuration/app-settings"
					},
                    {
						title: m.template_import_export(),
						url: "/dashboard/configuration/template-import-export"
					}
				]
			}
		],
		navSecondary: [
			{
				title: "Help Me",
				url: DOCUMENTATION_URL,
				icon: Book,
			}
		]
	};
</script>

<script lang="ts">
	import NavMain from "@/components/nav/nav-main.svelte";
	import NavSecondary from "@/components/nav/nav-secondary.svelte";
	import NavUser from "@/components/nav/nav-user.svelte";
	import * as Sidebar from "$lib/components/ui/sidebar/index.js";
	import CommandIcon from "@lucide/svelte/icons/command";
	import type { ComponentProps } from "svelte";
    import { userStore } from "@/stores/store-factory";
    import { m } from "@/paraglide/messages";

	let { ref = $bindable(null), ...restProps }: ComponentProps<typeof Sidebar.Root> = $props();
</script>

<Sidebar.Root bind:ref variant="inset" {...restProps}>
	<Sidebar.Header>
		<Sidebar.Menu>
			<Sidebar.MenuItem>
				<Sidebar.MenuButton size="lg">
					{#snippet child({ props })}
						<a href="/dashboard" {...props}>
							<div
								class="bg-sidebar-primary text-sidebar-primary-foreground flex aspect-square size-8 items-center justify-center rounded-lg"
							>
								<CommandIcon class="size-4" />
								<img 
									src={appLogo}
									alt="App logo"
								/>
							</div>
							<div class="grid flex-1 text-left text-sm leading-tight">
								<span class="truncate font-medium">{APP_NAME}</span>								
							</div>
						</a>
					{/snippet}
				</Sidebar.MenuButton>
			</Sidebar.MenuItem>
		</Sidebar.Menu>
	</Sidebar.Header>
	<Sidebar.Content>
		<NavMain items={data.navMain} />
		<NavSecondary items={data.navSecondary} class="mt-auto" />
	</Sidebar.Content>
	<Sidebar.Footer>
		<NavUser user={data.user} />
	</Sidebar.Footer>
</Sidebar.Root>
