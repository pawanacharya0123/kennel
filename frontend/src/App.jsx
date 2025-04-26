import { useEffect, useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import api from "./components/axiosConfig";
import { getAccessToken, getRefreshToken } from "./components/axiosConfig";

function App() {
  const [count, setCount] = useState(0);
  const [pets, setPets] = useState([]);

  const fetchPets = async () => {
    try {
      const accessToken = getAccessToken();
      const response = await api.get("/api/pets", {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setPets(response.data);
    } catch (error) {
      if (error.status === 401) {
        const refreshToken = getRefreshToken();
        try {
          const refreshResponse = await api.post("/refresh", {
            refreshToken: refreshToken,
          });

          const newAccessToken = refreshResponse.data.accessToken;
          localStorage.setItem("accessToken", newAccessToken);

          // Retry original request
          const retryResponse = await api.get("/api/pets", {
            headers: {
              Authorization: `Bearer ${newAccessToken}`,
            },
          });
          console.log("Retried Pets:", retryResponse.data);
          setPets(retryResponse.data);
        } catch (refreshError) {
          console.error("Refresh token failed:", refreshError);
        }
      } else {
        console.error("Error fetching pets", error);
      }
    }
  };
  useEffect(() => {
    fetchPets();
  }, []);

  return (
    <>
      <div className="p-4">
        <h1 className="text-xl font-bold mb-4">All Pets</h1>
        <ul className="space-y-2">
          {pets.map((pet) => (
            <li key={pet.id} className="bg-white shadow-md rounded p-3">
              <strong>{pet.name}</strong> – {pet.type} - Age:{pet.age}
            </li>
          ))}
        </ul>
      </div>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Kennel: Vite + React + Docker + SpringBoot</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  );
}

export default App;
