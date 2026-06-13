// src/components/FormularioOcorrencia.jsx
import { useState } from 'react';
import { enviarSolicitacao } from '../api/solicitacaoApi';

export default function FormularioOcorrencia() {
    const [tipo, setTipo] = useState('Buraco na Via');
    const [descricao, setDescricao] = useState('');
    
    const [status, setStatus] = useState('idle'); // 'idle' | 'loading' | 'success' | 'error'
    const [mensagemErro, setMensagemErro] = useState('');
    const [protocolo, setProtocolo] = useState('');

    const validarFormulario = () => {
        if (descricao.trim().length < 10) {
            setMensagemErro('A descrição deve conter pelo menos 10 caracteres para detalhar o problema.');
            return false;
        }
        setMensagemErro('');
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validarFormulario()) return;

        setStatus('loading');
        setMensagemErro('');

        try {
            const protocoloGerado = await enviarSolicitacao({ tipo, descricao });
            setProtocolo(protocoloGerado);
            setStatus('success');
        } catch (error) {
            setMensagemErro(error.message);
            setStatus('error');
        }
    };

    if (status === 'success') {
        return (
            <div className="bg-white rounded-2xl shadow-sm border border-green-200 p-8 text-center transition-all">
                <div className="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4">
                    <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="3" d="M5 13l4 4L19 7"></path></svg>
                </div>
                <h2 className="text-2xl font-bold text-slate-800 mb-2">Solicitação Enviada!</h2>
                <p className="text-slate-600 mb-6">Seu chamado foi registrado na Central.</p>
                <div className="bg-slate-50 border border-slate-200 rounded-xl py-3 px-6 inline-block mb-6">
                    <span className="text-xs font-bold text-slate-500 block mb-1">NÚMERO DO PROTOCOLO</span>
                    <span className="text-2xl font-black text-blue-700 tracking-widest font-mono">{protocolo}</span>
                </div>
                <div>
                    <button onClick={() => { setDescricao(''); setStatus('idle'); }} className="text-blue-600 font-semibold hover:underline">
                        Abrir novo chamado
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
            <div className="bg-slate-50 px-6 py-4 border-b border-slate-200">
                <h2 className="font-bold text-slate-800">Abertura de Solicitação Técnica</h2>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6 space-y-6">
                {mensagemErro && (
                    <div className="bg-red-50 text-red-700 p-4 rounded-lg text-sm border border-red-200 flex items-start gap-3">
                        <svg className="w-5 h-5 shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        <span>{mensagemErro}</span>
                    </div>
                )}

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm font-semibold text-slate-700 mb-2">Tipo de Ocorrência</label>
                        <select 
                            value={tipo}
                            onChange={(e) => setTipo(e.target.value)}
                            disabled={status === 'loading'}
                            className="w-full bg-white border border-slate-300 rounded-lg p-3 outline-none focus:ring-2 focus:ring-blue-500 transition-all"
                        >
                            <option>Buraco na Via</option>
                            <option>Iluminação Pública</option>
                            <option>Poda de Árvore</option>
                        </select>
                    </div>
                    <div>
                        <label className="block text-sm font-semibold text-slate-700 mb-2">Localização</label>
                        <input 
                            type="text" 
                            disabled 
                            value="Rua das Flores, 123 (via GPS)" 
                            className="w-full bg-slate-100 border border-slate-200 text-slate-500 rounded-lg p-3 outline-none italic text-sm"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Descrição Detalhada</label>
                    <textarea 
                        rows="3" 
                        value={descricao}
                        onChange={(e) => setDescricao(e.target.value)}
                        disabled={status === 'loading'}
                        className="w-full border border-slate-300 rounded-lg p-3 outline-none focus:ring-2 focus:ring-blue-500" 
                        placeholder="Descreva o problema aqui..."
                    />
                </div>

                <div className="flex justify-end pt-4">
                    <button 
                        type="submit" 
                        disabled={status === 'loading'}
                        className="w-full md:w-auto px-8 py-3 bg-blue-600 text-white font-bold rounded-lg hover:bg-blue-700 shadow-md shadow-blue-200 transition-all disabled:bg-blue-400 flex items-center justify-center gap-2"
                    >
                        {status === 'loading' ? (
                            <>
                                <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                                Processando...
                            </>
                        ) : 'Confirmar Envio'}
                    </button>
                </div>
            </form>
        </div>
    );
}
