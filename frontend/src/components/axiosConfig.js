// axiosConfig.js
import axios from "axios";

export const getAccessToken = () => localStorage.getItem("accessToken");
export const getRefreshToken = () => localStorage.getItem("refreshToken");

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // needed for sending refreshToken cookie
});

// api.interceptors.request.use((config) => {
//   const token = localStorage.getItem("accessToken");
//   if (token) {
//     config.headers.Authorization = `Bearer ${token}`;
//   }
//   return config;
// });

export default api;
