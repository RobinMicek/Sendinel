<script lang="ts">
    import { clearAuthInfo, tokenStore, userStore } from "@/stores/auth-store";
</script>
Token: {$tokenStore} <br>
User info: {$userStore?.firstname} <br>
<button on:click={clearAuthInfo}>Logout</button>