<script lang="ts">
	import ChevronsUpDownIcon from "@lucide/svelte/icons/chevrons-up-down";
	import LogOutIcon from "@lucide/svelte/icons/log-out";
	import Settings2 from "@lucide/svelte/icons/settings-2";
	import * as Avatar from "@/components/ui/avatar/index.js";
	import * as DropdownMenu from "@/components/ui/dropdown-menu/index.js";
	import * as Sidebar from "@/components/ui/sidebar/index.js";
	import { useSidebar } from "@/components/ui/sidebar/index.js";
    import { clearAuthInfo } from "@/stores/store-factory";
    import { goto } from "$app/navigation";
    import { m } from "@/paraglide/messages";
    import { getGravatarUrl } from "@/utils/gravatar-util";
    import { appSettingsStore } from "@/stores/store-factory";

	let {
		user,
	}: {
		user: {
			firstname: string;
			lastname: string;
			email: string;
		};
	} = $props();

	const sidebar = useSidebar();
</script>

<Sidebar.Menu>
	<Sidebar.MenuItem>
		<DropdownMenu.Root>
			<DropdownMenu.Trigger>
				{#snippet child({ props })}
					<Sidebar.MenuButton
						{...props}
						size="lg"
						class="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
					>
						<Avatar.Root class="size-8 rounded-lg">
							{#if appSettingsStore.get()?.useGravatar}
								<Avatar.Image src={getGravatarUrl(user.email, 100)} alt={user.firstname + " " + user.lastname} />
							{/if}
							<Avatar.Fallback class="rounded-lg">{user.firstname?.charAt(0) + user.lastname?.charAt(0)}</Avatar.Fallback>
						</Avatar.Root>
						<div class="grid flex-1 text-left text-sm leading-tight">
							<span class="truncate font-medium">{user.firstname} {user.lastname}</span>
							<span class="truncate text-xs">{user.email}</span>
						</div>
						<ChevronsUpDownIcon class="ml-auto size-4" />
					</Sidebar.MenuButton>
				{/snippet}
			</DropdownMenu.Trigger>
			<DropdownMenu.Content
				class="w-(--bits-dropdown-menu-anchor-width) min-w-56 rounded-lg"
				side={sidebar.isMobile ? "bottom" : "right"}
				align="end"
				sideOffset={4}
			>
				<DropdownMenu.Label class="p-0 font-normal">
					<div class="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
						<Avatar.Root class="size-8 rounded-lg">
							{#if appSettingsStore.get()?.useGravatar}
								<Avatar.Image src={getGravatarUrl(user.email, 100)} alt={user.firstname + " " + user.lastname} />
							{/if}
							<Avatar.Fallback class="rounded-lg">{user.firstname?.charAt(0) + user.lastname?.charAt(0)}</Avatar.Fallback>
						</Avatar.Root>
						<div class="grid flex-1 text-left text-sm leading-tight">
							<span class="truncate font-medium">{user.firstname} {user.lastname}</span>
							<span class="truncate text-xs">{user.email}</span>
						</div>
					</div>
				</DropdownMenu.Label>
				<DropdownMenu.Separator />
				<DropdownMenu.Group>
					<DropdownMenu.Item onclick={() => {goto("/dashboard/account-preferences")}}>
						<Settings2 />
						{m.account_preferences()}
					</DropdownMenu.Item>
				</DropdownMenu.Group>
				<DropdownMenu.Separator />
				<DropdownMenu.Item onclick={() => {clearAuthInfo(); goto("/")}}>
					<LogOutIcon />
					{m.logout()}
				</DropdownMenu.Item>
			</DropdownMenu.Content>
		</DropdownMenu.Root>
	</Sidebar.MenuItem>
</Sidebar.Menu>
