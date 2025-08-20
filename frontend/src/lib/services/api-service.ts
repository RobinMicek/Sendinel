import axios from 'axios';
import { tokenStore } from '@/stores/userinfo';

abstract class APIService {
	api = axios.create({
		baseURL: '/backend'
	});

	bearerToken = "Bearer " + tokenStore.get()

	constructor() {
		if (typeof process !== 'undefined' && process?.env?.DEVELOPMENT_BACKEND_URL) {
			this.api.defaults.baseURL = process.env.DEVELOPMENT_BACKEND_URL;
		}
	}
}

export default APIService;