// src/App.jsx
import FormularioOcorrencia from './components/FormularioOcorrencia';

function App() {
  return (
    <div className="min-h-screen bg-slate-50 font-sans text-slate-900">
      
      {/* Navbar Responsiva */}
      <nav className="bg-blue-800 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex items-center justify-between h-16">
          <span className="text-2xl font-black tracking-tighter">Hack<span className="text-blue-300">Gov</span></span>
          <div className="hidden md:flex space-x-4">
            <a href="#" className="px-3 py-2 rounded-md text-sm font-medium bg-blue-900">Painel de Ocorrências</a>
          </div>
        </div>
      </nav>

      {/* Header */}
      <header className="bg-white border-b border-slate-200 mb-8">
        <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
            <h1 className="text-3xl font-bold text-slate-900">Central de Serviços Urbanos</h1>
            <p className="mt-2 text-slate-600">Gestão inteligente para uma cidade melhor.</p>
        </div>
      </header>

      {/* Grid Principal */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-12">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Card Lateral */}
          <div className="lg:col-span-1">
             <div className="bg-gradient-to-br from-blue-600 to-blue-800 p-6 rounded-2xl text-white shadow-lg">
                <h3 className="font-bold text-lg mb-2">Transparência HackGov</h3>
                <p className="text-blue-100 text-sm">Esta semana já resolvemos mais de 120 chamados via plataforma preditiva.</p>
             </div>
          </div>

          {/* Área do Formulário */}
          <div className="lg:col-span-2">
            <FormularioOcorrencia />
          </div>

        </div>
      </main>

    </div>
  );
}

export default App;