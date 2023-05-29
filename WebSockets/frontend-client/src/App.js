import { Routes, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Home from "./pages/home";
import UserHomePage from './pages/user-home-page';

const App = () => {
  return (
     <>
        <Routes>
           <Route path="/" element={<Home />} />
           <Route path="/login" element={<UserHomePage />} />
        </Routes>
     </>
  );
 };
 

export default App;
